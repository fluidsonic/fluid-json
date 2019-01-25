package com.github.fluidsonic.fluid.json

import java.io.IOException


internal class StandardReader(private val source: TextInput) : JSONReader {

	private val buffer = StringBuilder()
	private var isClosed = false
	private var peekedToken: JSONToken? = null
	private var peekedTokenIndex = -1
	private var state = State()
	private val statePool: MutableList<State> = mutableListOf()
	private val stateStack: MutableList<State> = mutableListOf(state)


	override fun beginValueIsolation() {
		ensureNotClosed()
		check(!isInValueIsolation) { "Value isolation cannot begin multiple times" }
		check(state.tokenLocation.isBeforeValue) { "Value isolation can only begin right before a value" }

		state.isInValueIsolation = true
	}


	override fun close() {
		if (isClosed) return

		isClosed = true
		source.close()
	}


	private fun didReadValue() {
		state.tokenLocation = state.tokenLocation.afterValue
			?: error("Internal inconsistency - unexpected token location '${state.tokenLocation}'")

		if (state.isInValueIsolation) {
			state.hasReadIsolatedValue = true
		}
	}


	override fun endValueIsolation() {
		ensureNotClosed()
		check(isInValueIsolation) { "Value isolation cannot end as it has not begun" }
		check(state.hasReadIsolatedValue) { "No value has been read but exactly one has been expected" }

		state.hasReadIsolatedValue = false
		state.isInValueIsolation = false
	}


	private fun ensureNotClosed() {
		if (isClosed) throw IOException("Cannot operate on a closed JSON reader")
	}


	override val isInValueIsolation
		get() = state.isInValueIsolation


	private inline fun <Value> isolatedValueRead(checkBoundary: Boolean, crossinline read: () -> Value): Value {
		willReadValue()

		val value = read()

		if (checkBoundary) {
			val nextCharacter = source.peekCharacter()
			if (!JSONCharacter.isValueBoundary(nextCharacter)) {
				throw JSONException.forUnexpectedCharacter(
					nextCharacter,
					expected = "end of value",
					characterIndex = source.sourceIndex
				)
			}
		}

		didReadValue()

		return value
	}


	override val nextToken: JSONToken?
		get() {
			ensureNotClosed()

			if (peekedTokenIndex >= 0) {
				return peekedToken
			}

			val peekedToken = peekToken()
			this.peekedToken = peekedToken
			this.peekedTokenIndex = source.sourceIndex

			return peekedToken
		}


	override val path: JSONPath
		get() {
			if (isClosed) return JSONPath.root

			when (state.tokenLocation) {
				TokenLocation.afterRootValue,
				TokenLocation.beforeRootValue ->
					return JSONPath.root

				else ->
					return JSONPath(elements = stateStack.mapNotNull { it.toPathElement() })
			}
		}


	private fun peekToken(): JSONToken? {
		val source = source

		while (true) {
			source.skipWhitespaceCharacters()

			val character = source.peekCharacter()

			when (state.tokenLocation) {
				TokenLocation.afterListElement ->
					when (character) {
						JSONCharacter.Symbol.comma -> {
							source.readCharacter()

							state.currentValueListIndex += 1
							state.tokenLocation = TokenLocation.afterListElementSeparator
						}

						JSONCharacter.Symbol.rightSquareBracket -> {
							popState()

							return JSONToken.listEnd
						}

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "',' or ']'")
					}

				TokenLocation.afterListElementSeparator ->
					return peekValueToken(expected = "a value")

				TokenLocation.afterListStart ->
					when (character) {
						JSONCharacter.Symbol.rightSquareBracket -> {
							popState()

							return JSONToken.listEnd
						}
						else ->
							return peekValueToken(expected = "a value or ']'")
					}

				TokenLocation.afterMapElement ->
					when (character) {
						JSONCharacter.Symbol.comma -> {
							source.readCharacter()

							state.currentValueMapKey = null
							state.tokenLocation = TokenLocation.afterMapElementSeparator
						}

						JSONCharacter.Symbol.rightCurlyBracket -> {
							popState()

							return JSONToken.mapEnd
						}

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "',' or '}'")
					}

				TokenLocation.afterMapElementSeparator ->
					when (character) {
						JSONCharacter.Symbol.quotationMark ->
							return JSONToken.mapKey

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "'\"'")
					}

				TokenLocation.afterMapKey ->
					when (character) {
						JSONCharacter.Symbol.colon -> {
							source.readCharacter()

							state.tokenLocation = TokenLocation.afterMapKeySeparator
						}

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "':'")
					}

				TokenLocation.afterMapKeySeparator ->
					return peekValueToken(expected = "a value")

				TokenLocation.afterMapStart ->
					when (character) {
						JSONCharacter.Symbol.quotationMark ->
							return JSONToken.mapKey

						JSONCharacter.Symbol.rightCurlyBracket -> {
							popState()

							return JSONToken.mapEnd
						}

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "'\"' or '}'")
					}

				TokenLocation.afterRootValue ->
					when (character) {
						JSONCharacter.end ->
							return null

						else ->
							throw JSONException.forUnexpectedCharacter(character, expected = "end of input")
					}

				TokenLocation.beforeRootValue ->
					return peekValueToken(expected = "a value")
			}
		}
	}


	private fun peekValueToken(expected: String): JSONToken? {
		val character = source.peekCharacter()
		return when (character) {
			JSONCharacter.Symbol.quotationMark ->
				JSONToken.stringValue

			JSONCharacter.Symbol.hyphenMinus,
			JSONCharacter.Digit.zero,
			JSONCharacter.Digit.one,
			JSONCharacter.Digit.two,
			JSONCharacter.Digit.three,
			JSONCharacter.Digit.four,
			JSONCharacter.Digit.five,
			JSONCharacter.Digit.six,
			JSONCharacter.Digit.seven,
			JSONCharacter.Digit.eight,
			JSONCharacter.Digit.nine ->
				JSONToken.numberValue

			JSONCharacter.Letter.f,
			JSONCharacter.Letter.t ->
				JSONToken.booleanValue

			JSONCharacter.Letter.n ->
				JSONToken.nullValue

			JSONCharacter.Symbol.leftCurlyBracket -> {
				pushNewState(TokenLocation.afterMapStart)

				JSONToken.mapStart
			}

			JSONCharacter.Symbol.leftSquareBracket -> {
				pushNewState(TokenLocation.afterListStart)

				JSONToken.listStart
			}

			else ->
				throw JSONException.forUnexpectedCharacter(character, expected = expected)
		}
	}


	private fun popState() {
		statePool += stateStack.removeAt(stateStack.size - 1)
		state = stateStack.last()
	}


	private fun pushNewState(tokenLocation: TokenLocation) {
		val newState = statePool.getOrElse(statePool.size - 1) { State() }
		newState.reset(tokenLocation = tokenLocation)

		state = newState
		stateStack += newState
	}


	override fun readBoolean(): Boolean {
		readToken(JSONToken.booleanValue)

		return isolatedValueRead(checkBoundary = true) {
			source.run {
				if (peekCharacter() == JSONCharacter.Letter.t) {
					readCharacter(JSONCharacter.Letter.t)
					readCharacter(JSONCharacter.Letter.r)
					readCharacter(JSONCharacter.Letter.u)
					readCharacter(JSONCharacter.Letter.e)

					true
				}
				else {
					readCharacter(JSONCharacter.Letter.f)
					readCharacter(JSONCharacter.Letter.a)
					readCharacter(JSONCharacter.Letter.l)
					readCharacter(JSONCharacter.Letter.s)
					readCharacter(JSONCharacter.Letter.e)

					false
				}
			}
		}
	}


	override fun readDouble(): Double {
		readNumberIntoBuffer()

		return buffer.toString().toDouble()
	}


	override fun readFloat(): Float {
		readNumberIntoBuffer()

		return buffer.toString().toFloat()
	}


	override fun readListEnd() {
		readToken(JSONToken.listEnd)

		source.readCharacter(JSONCharacter.Symbol.rightSquareBracket)

		didReadValue()
	}


	override fun readListStart() {
		readToken(JSONToken.listStart)

		willReadValue()

		source.readCharacter(JSONCharacter.Symbol.leftSquareBracket)

		state.currentValueListIndex = 0
	}


	override fun readLong(): Long {
		readToken(JSONToken.numberValue)

		return source.locked { readLongLocked() }
	}


	private fun readLongLocked(): Long =
		isolatedValueRead(checkBoundary = true) {
			val startIndex = source.index

			val isNegative: Boolean
			val negativeLimit: Long

			val source = source
			var character = source.readCharacter()
			if (character == JSONCharacter.Symbol.hyphenMinus) {
				isNegative = true
				character = source.readCharacter(required = JSONCharacter::isDigit) { "a digit" }
				negativeLimit = Long.MIN_VALUE
			}
			else {
				isNegative = false
				negativeLimit = -Long.MAX_VALUE
			}

			val minimumBeforeMultiplication = negativeLimit / 10
			var value = 0L

			if (character == JSONCharacter.Digit.zero) {
				character = source.readCharacter(required = { !JSONCharacter.isDigit(it) }) {
					JSONCharacter.toString(
						JSONCharacter.Symbol.fullStop,
						JSONCharacter.Letter.e,
						JSONCharacter.Letter.E
					) + " or end of number after a leading '0'"
				}
			}
			else {
				do {
					val digit = character - JSONCharacter.Digit.zero
					if (value < minimumBeforeMultiplication) {
						value = negativeLimit

						do character = source.readCharacter()
						while (JSONCharacter.isDigit(character))
						break
					}

					value *= 10

					if (value < negativeLimit + digit) {
						value = negativeLimit

						do character = source.readCharacter()
						while (JSONCharacter.isDigit(character))
						break
					}

					value -= digit
					character = source.readCharacter()
				}
				while (JSONCharacter.isDigit(character))

				if (!isNegative) {
					value *= -1
				}
			}

			if (character == JSONCharacter.Symbol.fullStop) { // truncate decimal value
				source.readCharacter(required = JSONCharacter::isDigit) { "a digit in decimal value of number" }

				do character = source.readCharacter()
				while (JSONCharacter.isDigit(character))
			}

			if (character == JSONCharacter.Letter.e || character == JSONCharacter.Letter.E) { // oh no, an exponent!
				source.seekTo(startIndex)
				unreadToken(JSONToken.numberValue)

				return@isolatedValueRead readDouble().toLong()
			}

			source.seekBackOneCharacter()

			return@isolatedValueRead value
		}


	override fun readMapEnd() {
		readToken(JSONToken.mapEnd)

		source.readCharacter(JSONCharacter.Symbol.rightCurlyBracket)

		didReadValue()
	}


	override fun readMapStart() {
		readToken(JSONToken.mapStart)

		willReadValue()

		source.readCharacter(JSONCharacter.Symbol.leftCurlyBracket)
	}


	override fun readNull(): Nothing? {
		readToken(JSONToken.nullValue)

		return isolatedValueRead(checkBoundary = true) {
			source.run {
				readCharacter(JSONCharacter.Letter.n)
				readCharacter(JSONCharacter.Letter.u)
				readCharacter(JSONCharacter.Letter.l)
				readCharacter(JSONCharacter.Letter.l)

				null
			}
		}
	}


	override fun readNumber(): Number {
		val shouldParseAsFloatingPoint = readNumberIntoBuffer()

		val stringValue = buffer.toString()
		if (!shouldParseAsFloatingPoint) {
			val value = stringValue.toLongOrNull()
			if (value != null) {
				return if (value in Int.MIN_VALUE .. Int.MAX_VALUE) value.toInt() else value
			}
		}

		return stringValue.toDouble()
	}


	private fun readNumberIntoBuffer(): Boolean {
		readToken(JSONToken.numberValue)

		return isolatedValueRead(checkBoundary = true) {
			val buffer = buffer
			buffer.setLength(0)

			var shouldParseAsFloatingPoint = false
			val source = source
			var character = source.readCharacter()

			// consume optional minus sign
			if (character == JSONCharacter.Symbol.hyphenMinus) {
				buffer.append('-')
				character = source.readCharacter()
			}

			// consume integer value
			when (character) {
				JSONCharacter.Digit.zero -> {
					buffer.append('0')
					character = source.readCharacter(required = { !JSONCharacter.isDigit(it) }) {
						JSONCharacter.toString(
							JSONCharacter.Symbol.fullStop,
							JSONCharacter.Letter.e,
							JSONCharacter.Letter.E
						) + " or end of number after a leading '0'"
					}
				}

				JSONCharacter.Digit.one,
				JSONCharacter.Digit.two,
				JSONCharacter.Digit.three,
				JSONCharacter.Digit.four,
				JSONCharacter.Digit.five,
				JSONCharacter.Digit.six,
				JSONCharacter.Digit.seven,
				JSONCharacter.Digit.eight,
				JSONCharacter.Digit.nine ->
					do {
						buffer.append(character.toChar())
						character = source.readCharacter()
					}
					while (JSONCharacter.isDigit(character))

				else ->
					throw JSONException.forUnexpectedCharacter(
						character,
						expected = "a digit in integer value of number",
						characterIndex = source.sourceIndex - 1
					)
			}

			// consume optional decimal separator and value
			if (character == JSONCharacter.Symbol.fullStop) {
				shouldParseAsFloatingPoint = true

				buffer.append('.')
				character = source.readCharacter(required = JSONCharacter::isDigit) { "a digit in decimal value of number" }

				do {
					buffer.append(character.toChar())
					character = source.readCharacter()
				}
				while (JSONCharacter.isDigit(character))
			}

			// consume optional exponent separator and value
			if (character == JSONCharacter.Letter.e || character == JSONCharacter.Letter.E) {
				shouldParseAsFloatingPoint = true

				buffer.append(character.toChar())

				character = source.peekCharacter()
				if (character == JSONCharacter.Symbol.plusSign || character == JSONCharacter.Symbol.hyphenMinus) {
					buffer.append(character.toChar())
					source.readCharacter()
				}

				character = source.readCharacter(required = JSONCharacter::isDigit) { "a digit in exponent value of number" }

				do {
					buffer.append(character.toChar())
					character = source.readCharacter()
				}
				while (JSONCharacter.isDigit(character))
			}

			source.seekBackOneCharacter()

			return@isolatedValueRead shouldParseAsFloatingPoint
		}
	}


	override fun readString(): String {
		val token = readToken(JSONToken.stringValue, alternative = JSONToken.mapKey)

		return isolatedValueRead(checkBoundary = false) {
			val string = source.locked { readStringLocked() }

			if (token == JSONToken.mapKey) {
				state.currentValueMapKey = string
			}

			return@isolatedValueRead string
		}
	}


	private fun readStringLocked(): String {
		val source = source
		source.readCharacter(JSONCharacter.Symbol.quotationMark)

		val buffer = buffer
		buffer.setLength(0)

		var startIndex = source.index

		do {
			var character = source.readCharacter()
			when (character) {
				JSONCharacter.Symbol.reverseSolidus -> {
					val endIndex = source.index - 1
					if (endIndex > startIndex) {
						buffer.append(source.buffer, startIndex, endIndex - startIndex)
					}

					character = source.readCharacter()
					when (character) {
						JSONCharacter.Symbol.reverseSolidus,
						JSONCharacter.Symbol.solidus ->
							buffer.append(character.toChar())

						JSONCharacter.Symbol.quotationMark -> {
							buffer.append(character.toChar())
							character = 0
						}

						JSONCharacter.Letter.b -> buffer.append('\b')
						JSONCharacter.Letter.f -> buffer.append('\u000C')
						JSONCharacter.Letter.n -> buffer.append('\n')
						JSONCharacter.Letter.r -> buffer.append('\r')
						JSONCharacter.Letter.t -> buffer.append('\t')
						JSONCharacter.Letter.u -> {
							val digit1 = source.readCharacter(required = JSONCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit2 = source.readCharacter(required = JSONCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit3 = source.readCharacter(required = JSONCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit4 = source.readCharacter(required = JSONCharacter::isHexDigit) { "0-9, a-f or A-F" }

							val decodedCharacter = (JSONCharacter.parseHexDigit(digit1) shl 12) or
								(JSONCharacter.parseHexDigit(digit2) shl 8) or
								(JSONCharacter.parseHexDigit(digit3) shl 4) or
								JSONCharacter.parseHexDigit(digit4)

							buffer.append(decodedCharacter.toChar())
						}
						else -> throw  JSONException.forUnexpectedCharacter(character, "an escape sequence starting with '\\', '/', '\"', 'b', 'f', 'n', 'r', 't' or 'u'")
					}

					startIndex = source.index
				}

				0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F ->
					throw JSONException.forUnexpectedCharacter(character, "an escape sequence")

				JSONCharacter.Symbol.quotationMark ->
					Unit

				JSONCharacter.end ->
					throw JSONException("unterminated string value")
			}
		}
		while (character != JSONCharacter.Symbol.quotationMark)

		val endIndex = source.index - 1
		if (endIndex > startIndex) {
			buffer.append(source.buffer, startIndex, endIndex - startIndex)
		}

		return buffer.toString()
	}


	private fun readToken(required: JSONToken) {
		val token = nextToken
		if (token != required) {
			throw JSONException.forUnexpectedToken(
				token,
				expected = "'$required'",
				characterIndex = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1
	}


	@Suppress("SameParameterValue")
	private fun <Token : JSONToken?> readToken(required: Token, alternative: Token): Token {
		val token = nextToken
		if (token != required && token != alternative) {
			throw JSONException.forUnexpectedToken(
				token,
				expected = "'$required' or '$alternative'",
				characterIndex = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1

		@Suppress("UNCHECKED_CAST")
		return token as Token
	}


	override fun terminate() {
		ensureNotClosed()
		val nextToken = nextToken

		close()

		if (nextToken != null)
			throw JSONException("Expected end of input but found token $nextToken")
	}


	@Suppress("SameParameterValue")
	private fun unreadToken(token: JSONToken) {
		peekedToken = token
		peekedTokenIndex = source.sourceIndex
	}


	private fun willReadValue() {
		check(!state.isInValueIsolation || !state.hasReadIsolatedValue) { "Cannot read more than one value in the current value isolation context" }
	}


	private fun JSONException.Companion.forUnexpectedCharacter(character: Int, expected: String, characterIndex: Int = source.sourceIndex) =
		JSONException(
			if (characterIndex == 0 && character == JSONCharacter.end)
				"Cannot parse empty JSON data"
			else
				"(UTF-16 offset $characterIndex, in $path) unexpected ${JSONCharacter.toString(character)}, expected $expected"
		)


	private fun JSONException.Companion.forUnexpectedToken(token: JSONToken?, expected: String, characterIndex: Int = source.sourceIndex): JSONException {
		val tokenString = if (token != null) "'$token'" else "<end of input>"
		return JSONException("(UTF-16 offset $characterIndex, in $path) unexpected token $tokenString, expected $expected")
	}


	private fun TextInput.readCharacter(required: Int) =
		readCharacter(required) { JSONCharacter.toString(required) }


	private inline fun TextInput.readCharacter(required: Int, crossinline expected: () -> String): Int {
		// contract {
		// 	callsInPlace(expected, InvocationKind.AT_MOST_ONCE)
		// }

		return readCharacter(required = { it == required }, expected = expected)
	}


	private inline fun TextInput.readCharacter(required: (character: Int) -> Boolean, crossinline expected: () -> String): Int {
		// contract {
		// 	callsInPlace(expected, InvocationKind.AT_MOST_ONCE)
		// 	callsInPlace(required, InvocationKind.EXACTLY_ONCE)
		// }

		val character = readCharacter()
		if (!required(character)) {
			throw JSONException.forUnexpectedCharacter(
				character,
				expected = expected(),
				characterIndex = index - 1
			)
		}

		return character
	}


	private class State {

		var currentValueListIndex = -1
		var currentValueMapKey: String? = null
		var hasReadIsolatedValue = false
		var isInValueIsolation = false
		var tokenLocation = TokenLocation.beforeRootValue


		fun reset(tokenLocation: TokenLocation) {
			this.tokenLocation = tokenLocation

			currentValueListIndex = -1
			currentValueMapKey = null
			hasReadIsolatedValue = false
			isInValueIsolation = false
		}


		fun toPathElement(): JSONPath.Element? {
			if (currentValueListIndex >= 0) return JSONPath.Element.ListIndex(currentValueListIndex)
			currentValueMapKey?.let { return JSONPath.Element.MapKey(it) }

			return null
		}
	}


	private enum class TokenLocation(
		val afterValue: TokenLocation? = null
	) {

		afterListElement,
		afterListElementSeparator(afterValue = afterListElement),
		afterListStart(afterValue = afterListElement),
		afterMapElement,
		afterMapKey,
		afterMapElementSeparator(afterValue = afterMapKey),
		afterMapKeySeparator(afterValue = afterMapElement),
		afterMapStart(afterValue = afterMapKey),
		afterRootValue,
		beforeRootValue(afterValue = afterRootValue);


		val isBeforeValue
			get() = (afterValue != null)
	}
}
