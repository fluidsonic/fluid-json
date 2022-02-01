package io.fluidsonic.json


internal class StandardReader(private val source: TextInput) : JsonReader {

	private val buffer = StringBuilder()
	private var isClosed = false
	private var peekedToken: JsonToken? = null
	private var peekedTokenIndex = -1
	private var state = State()
	private val stateCache: MutableList<State> = mutableListOf()
	private val stateStack: MutableList<State> = mutableListOf(state)


	override fun beginValueIsolation(): JsonDepth {
		ensureNotClosed()

		valueIsolationCheck(peekTokenIfNeeded() != null) { "the root value has already been read" }
		valueIsolationCheck(state.tokenLocation.isBeforeValue) { "the next token is not a value but '${state.tokenLocation}'" }
		valueIsolationCheck(!state.isInValueIsolation || !state.hasReadIsolatedValue) { "cannot begin before previous one has ended" }

		state.valueIsolationCount += 1

		return depth
	}


	override fun close() {
		if (isClosed) return

		isClosed = true
		source.close()
	}


	override val depth
		get() = JsonDepth(stateStack.size - 1)


	private fun didReadValue() {
		state.tokenLocation = state.tokenLocation.afterValue
			?: parsingError("Internal inconsistency: unexpected token location '${state.tokenLocation}' after reading value")

		if (state.isInValueIsolation) {
			state.hasReadIsolatedValue = true
		}
	}


	override fun endValueIsolation(depth: JsonDepth) {
		ensureNotClosed()

		valueIsolationCheck(depth <= this.depth) { "lists or maps have been ended prematurely" }
		valueIsolationCheck(this.depth <= depth) { "lists or maps have not been ended properly" }
		valueIsolationCheck(isInValueIsolation) { "cannot end isolation - it either hasn't begun or been ended already" }
		valueIsolationCheck(state.hasReadIsolatedValue) { "exactly one value has been expected but none was read" }

		val valueIsolationCount = state.valueIsolationCount - 1
		state.valueIsolationCount = valueIsolationCount

		if (valueIsolationCount == 0) {
			state.hasReadIsolatedValue = false
		}
	}


	private fun ensureNotClosed() {
		parsingCheck(!isClosed) { "Cannot operate on a closed JsonReader" }
	}


	override val isInValueIsolation
		get() = state.isInValueIsolation


	override val nextToken: JsonToken?
		get() {
			ensureNotClosed()

			return peekTokenIfNeeded()
		}


	override val offset
		get() = if (peekedTokenIndex >= 0) peekedTokenIndex else source.sourceIndex


	private fun parseLong(): Long {
		val startIndex = source.index

		val isNegative: Boolean
		val negativeLimit: Long

		val source = source
		var character = source.readCharacter()
		if (character == JsonCharacter.Symbol.hyphenMinus) {
			isNegative = true
			character = source.readCharacter(required = JsonCharacter::isDigit) { "a digit" }
			negativeLimit = Long.MIN_VALUE
		}
		else {
			isNegative = false
			negativeLimit = -Long.MAX_VALUE
		}

		val minimumBeforeMultiplication = negativeLimit / 10
		var value = 0L

		if (character == JsonCharacter.Digit.zero) {
			character = source.readCharacter(required = { !JsonCharacter.isDigit(it) }) {
				JsonCharacter.toString(
					JsonCharacter.Symbol.fullStop,
					JsonCharacter.Letter.e,
					JsonCharacter.Letter.E
				) + " or end of number after a leading '0'"
			}
		}
		else {
			do {
				val digit = character - JsonCharacter.Digit.zero
				if (value < minimumBeforeMultiplication) {
					value = negativeLimit

					do character = source.readCharacter()
					while (JsonCharacter.isDigit(character))
					break
				}

				value *= 10

				if (value < negativeLimit + digit) {
					value = negativeLimit

					do character = source.readCharacter()
					while (JsonCharacter.isDigit(character))
					break
				}

				value -= digit
				character = source.readCharacter()
			}
			while (JsonCharacter.isDigit(character))

			if (!isNegative) {
				value *= -1
			}
		}

		if (character == JsonCharacter.Symbol.fullStop) { // truncate decimal value
			source.readCharacter(required = JsonCharacter::isDigit) { "a digit in decimal value of number" }

			do character = source.readCharacter()
			while (JsonCharacter.isDigit(character))
		}

		if (character == JsonCharacter.Letter.e || character == JsonCharacter.Letter.E) { // oh no, an exponent!
			source.seekTo(startIndex)
			parseNumberIntoBuffer()

			return buffer.toString().toDouble().toLong()
		}

		source.seekBackOneCharacter()

		return value
	}


	private fun parseNumberIntoBuffer(): Boolean {
		val buffer = buffer
		buffer.setLength(0)

		var shouldParseAsFloatingPoint = false
		val source = source
		var character = source.readCharacter()

		// consume optional minus sign
		if (character == JsonCharacter.Symbol.hyphenMinus) {
			buffer.append('-')
			character = source.readCharacter()
		}

		// consume integer value
		when (character) {
			JsonCharacter.Digit.zero -> {
				buffer.append('0')
				character = source.readCharacter(required = { !JsonCharacter.isDigit(it) }) {
					JsonCharacter.toString(
						JsonCharacter.Symbol.fullStop,
						JsonCharacter.Letter.e,
						JsonCharacter.Letter.E
					) + " or end of number after a leading '0'"
				}
			}

			JsonCharacter.Digit.one,
			JsonCharacter.Digit.two,
			JsonCharacter.Digit.three,
			JsonCharacter.Digit.four,
			JsonCharacter.Digit.five,
			JsonCharacter.Digit.six,
			JsonCharacter.Digit.seven,
			JsonCharacter.Digit.eight,
			JsonCharacter.Digit.nine ->
				do {
					buffer.append(character.toChar())
					character = source.readCharacter()
				}
				while (JsonCharacter.isDigit(character))

			else ->
				unexpectedCharacterError(
					actual = character,
					expected = "a digit in integer value of number",
					offset = source.sourceIndex - 1
				)
		}

		// consume optional decimal separator and value
		if (character == JsonCharacter.Symbol.fullStop) {
			shouldParseAsFloatingPoint = true

			buffer.append('.')
			character = source.readCharacter(required = JsonCharacter::isDigit) { "a digit in decimal value of number" }

			do {
				buffer.append(character.toChar())
				character = source.readCharacter()
			}
			while (JsonCharacter.isDigit(character))
		}

		// consume optional exponent separator and value
		if (character == JsonCharacter.Letter.e || character == JsonCharacter.Letter.E) {
			shouldParseAsFloatingPoint = true

			buffer.append(character.toChar())

			character = source.peekCharacter()
			if (character == JsonCharacter.Symbol.plusSign || character == JsonCharacter.Symbol.hyphenMinus) {
				buffer.append(character.toChar())
				source.readCharacter()
			}

			character = source.readCharacter(required = JsonCharacter::isDigit) { "a digit in exponent value of number" }

			do {
				buffer.append(character.toChar())
				character = source.readCharacter()
			}
			while (JsonCharacter.isDigit(character))
		}

		source.seekBackOneCharacter()

		return shouldParseAsFloatingPoint
	}


	private inline fun parsingCheck(value: Boolean, lazyMessage: () -> String) {
		// contract {
		//  returns() implies value
		// }

		if (!value) parsingError(lazyMessage())
	}


	private fun parsingError(message: String): Nothing {
		throw JsonException.Parsing(
			message = message,
			offset = offset,
			path = path
		)
	}


	override val path: JsonPath
		get() {
			if (isClosed) return JsonPath.root

			return when (state.tokenLocation) {
				TokenLocation.afterRootValue,
				TokenLocation.beforeRootValue ->
					JsonPath.root

				else ->
					JsonPath(elements = stateStack.mapNotNull { it.toPathElement() })
			}
		}


	private fun peekToken(): JsonToken? {
		val source = source

		while (true) {
			source.skipWhitespaceCharacters()

			val character = source.peekCharacter()

			when (state.tokenLocation) {
				TokenLocation.afterListElement ->
					when (character) {
						JsonCharacter.Symbol.comma -> {
							source.readCharacter()

							state.currentValueListIndex += 1
							state.tokenLocation = TokenLocation.afterListElementSeparator
						}

						JsonCharacter.Symbol.rightSquareBracket ->
							return JsonToken.listEnd

						else ->
							unexpectedCharacterError(actual = character, expected = "',' or ']'")
					}

				TokenLocation.afterListElementSeparator ->
					return peekValueToken(expected = "a value")

				TokenLocation.afterListStart ->
					return when (character) {
						JsonCharacter.Symbol.rightSquareBracket ->
							JsonToken.listEnd

						else ->
							peekValueToken(expected = "a value or ']'")
					}

				TokenLocation.afterMapElement ->
					when (character) {
						JsonCharacter.Symbol.comma -> {
							source.readCharacter()

							state.currentValueMapKey = null
							state.tokenLocation = TokenLocation.afterMapElementSeparator
						}

						JsonCharacter.Symbol.rightCurlyBracket ->
							return JsonToken.mapEnd

						else ->
							unexpectedCharacterError(actual = character, expected = "',' or '}'")
					}

				TokenLocation.afterMapElementSeparator ->
					when (character) {
						JsonCharacter.Symbol.quotationMark ->
							return JsonToken.mapKey

						else ->
							unexpectedCharacterError(actual = character, expected = "'\"'")
					}

				TokenLocation.afterMapKey ->
					when (character) {
						JsonCharacter.Symbol.colon -> {
							source.readCharacter()

							state.tokenLocation = TokenLocation.afterMapKeySeparator
						}

						else ->
							unexpectedCharacterError(actual = character, expected = "':'")
					}

				TokenLocation.afterMapKeySeparator ->
					return peekValueToken(expected = "a value")

				TokenLocation.afterMapStart ->
					return when (character) {
						JsonCharacter.Symbol.quotationMark ->
							JsonToken.mapKey

						JsonCharacter.Symbol.rightCurlyBracket ->
							JsonToken.mapEnd

						else ->
							unexpectedCharacterError(actual = character, expected = "'\"' or '}'")
					}

				TokenLocation.afterRootValue ->
					when (character) {
						JsonCharacter.end ->
							return null

						else ->
							unexpectedCharacterError(actual = character, expected = "end of input")
					}

				TokenLocation.beforeRootValue ->
					return peekValueToken(expected = "a value")
			}
		}
	}


	private fun peekTokenIfNeeded(): JsonToken? {
		if (peekedTokenIndex >= 0) {
			return peekedToken
		}

		val peekedToken = peekToken()
		this.peekedToken = peekedToken
		this.peekedTokenIndex = source.sourceIndex

		return peekedToken
	}


	private fun peekValueToken(expected: String): JsonToken =
		when (val character = source.peekCharacter()) {
			JsonCharacter.Symbol.quotationMark ->
				JsonToken.stringValue

			JsonCharacter.Symbol.hyphenMinus,
			JsonCharacter.Digit.zero,
			JsonCharacter.Digit.one,
			JsonCharacter.Digit.two,
			JsonCharacter.Digit.three,
			JsonCharacter.Digit.four,
			JsonCharacter.Digit.five,
			JsonCharacter.Digit.six,
			JsonCharacter.Digit.seven,
			JsonCharacter.Digit.eight,
			JsonCharacter.Digit.nine ->
				JsonToken.numberValue

			JsonCharacter.Letter.f,
			JsonCharacter.Letter.t ->
				JsonToken.booleanValue

			JsonCharacter.Letter.n ->
				JsonToken.nullValue

			JsonCharacter.Symbol.leftCurlyBracket ->
				JsonToken.mapStart

			JsonCharacter.Symbol.leftSquareBracket ->
				JsonToken.listStart

			else ->
				unexpectedCharacterError(actual = character, expected = expected)
		}


	private fun popState() {
		valueIsolationCheck(!state.isInValueIsolation || state.hasReadIsolatedValue) { "cannot end a list or map since a value is still being expected" }
		valueIsolationCheck(!state.isInValueIsolation || !state.hasReadIsolatedValue) { "list or map is being ended prematurely" }

		stateCache += stateStack.removeAt(stateStack.size - 1)
		state = stateStack.last()
	}


	private fun pushState(tokenLocation: TokenLocation) {
		parsingCheck(tokenLocation.isBeforeValue) { "Internal inconsistency: cannot push state except at the beginning of a value" }

		val newState = if (stateCache.isNotEmpty()) stateCache.removeAt(stateCache.size - 1) else State()
		newState.reset(tokenLocation = tokenLocation)

		state = newState
		stateStack += newState
	}


	override fun readBoolean(): Boolean {
		readToken(JsonToken.booleanValue)

		return readValue(checkBoundary = true) {
			source.run {
				if (peekCharacter() == JsonCharacter.Letter.t) {
					readCharacter(JsonCharacter.Letter.t)
					readCharacter(JsonCharacter.Letter.r)
					readCharacter(JsonCharacter.Letter.u)
					readCharacter(JsonCharacter.Letter.e)

					true
				}
				else {
					readCharacter(JsonCharacter.Letter.f)
					readCharacter(JsonCharacter.Letter.a)
					readCharacter(JsonCharacter.Letter.l)
					readCharacter(JsonCharacter.Letter.s)
					readCharacter(JsonCharacter.Letter.e)

					false
				}
			}
		}
	}


	override fun readDouble(): Double {
		readToken(JsonToken.numberValue)

		return readValue(checkBoundary = true) {
			parseNumberIntoBuffer()

			buffer.toString().toDouble()
		}
	}


	override fun readFloat(): Float {
		readToken(JsonToken.numberValue)

		return readValue(checkBoundary = true) {
			parseNumberIntoBuffer()

			buffer.toString().toFloat()
		}
	}


	override fun readListEnd() {
		readToken(JsonToken.listEnd)

		source.readCharacter(JsonCharacter.Symbol.rightSquareBracket)

		popState()
		didReadValue()
	}


	override fun readListStart() {
		readToken(JsonToken.listStart)

		willReadValue()
		pushState(tokenLocation = TokenLocation.afterListStart)

		source.readCharacter(JsonCharacter.Symbol.leftSquareBracket)

		state.currentValueListIndex = 0
	}


	override fun readLong(): Long {
		readToken(JsonToken.numberValue)

		return readValue(checkBoundary = true) {
			source.locked { parseLong() }
		}
	}


	override fun readMapEnd() {
		readToken(JsonToken.mapEnd)

		source.readCharacter(JsonCharacter.Symbol.rightCurlyBracket)

		popState()
		didReadValue()
	}


	override fun readMapStart() {
		readToken(JsonToken.mapStart)

		willReadValue()
		pushState(tokenLocation = TokenLocation.afterMapStart)

		source.readCharacter(JsonCharacter.Symbol.leftCurlyBracket)
	}


	override fun readNull(): Nothing? {
		readToken(JsonToken.nullValue)

		return readValue(checkBoundary = true) {
			source.run {
				readCharacter(JsonCharacter.Letter.n)
				readCharacter(JsonCharacter.Letter.u)
				readCharacter(JsonCharacter.Letter.l)
				readCharacter(JsonCharacter.Letter.l)

				null
			}
		}
	}


	override fun readNumber(): Number {
		readToken(JsonToken.numberValue)

		return readValue(checkBoundary = true) {
			val shouldParseAsFloatingPoint = parseNumberIntoBuffer()

			val stringValue = buffer.toString()
			if (!shouldParseAsFloatingPoint) {
				val value = stringValue.toLongOrNull()
				if (value != null) {
					return@readValue if (value in Int.MIN_VALUE .. Int.MAX_VALUE) value.toInt() else value
				}
			}

			return@readValue stringValue.toDouble()
		}
	}


	override fun readString(): String {
		val token = readToken(JsonToken.stringValue, alternative = JsonToken.mapKey)

		return readValue(checkBoundary = false) {
			val string = source.locked { readStringLocked() }

			if (token == JsonToken.mapKey) {
				state.currentValueMapKey = string
			}

			return@readValue string
		}
	}


	private fun readStringLocked(): String {
		val source = source
		source.readCharacter(JsonCharacter.Symbol.quotationMark)

		val buffer = buffer
		buffer.setLength(0)

		var startIndex = source.index

		do {
			var character = source.readCharacter()
			when (character) {
				JsonCharacter.Symbol.reverseSolidus -> {
					val endIndex = source.index - 1
					if (endIndex > startIndex) {
						buffer.append(source.buffer, startIndex, endIndex - startIndex)
					}

					character = source.readCharacter()
					when (character) {
						JsonCharacter.Symbol.reverseSolidus,
						JsonCharacter.Symbol.solidus ->
							buffer.append(character.toChar())

						JsonCharacter.Symbol.quotationMark -> {
							buffer.append(character.toChar())
							character = 0
						}

						JsonCharacter.Letter.b -> buffer.append('\b')
						JsonCharacter.Letter.f -> buffer.append('\u000C')
						JsonCharacter.Letter.n -> buffer.append('\n')
						JsonCharacter.Letter.r -> buffer.append('\r')
						JsonCharacter.Letter.t -> buffer.append('\t')
						JsonCharacter.Letter.u -> {
							val digit1 = source.readCharacter(required = JsonCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit2 = source.readCharacter(required = JsonCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit3 = source.readCharacter(required = JsonCharacter::isHexDigit) { "0-9, a-f or A-F" }
							val digit4 = source.readCharacter(required = JsonCharacter::isHexDigit) { "0-9, a-f or A-F" }

							val decodedCharacter = (JsonCharacter.parseHexDigit(digit1) shl 12) or
								(JsonCharacter.parseHexDigit(digit2) shl 8) or
								(JsonCharacter.parseHexDigit(digit3) shl 4) or
								JsonCharacter.parseHexDigit(digit4)

							buffer.append(decodedCharacter.toChar())
						}
						else -> unexpectedCharacterError(
							actual = character,
							expected = "an escape sequence starting with '\\', '/', '\"', 'b', 'f', 'n', 'r', 't' or 'u'"
						)
					}

					startIndex = source.index
				}

				0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F ->
					unexpectedCharacterError(actual = character, expected = "an escape sequence")

				JsonCharacter.Symbol.quotationMark ->
					Unit

				JsonCharacter.end ->
					unexpectedCharacterError(actual = character, expected = "\"")
			}
		}
		while (character != JsonCharacter.Symbol.quotationMark)

		val endIndex = source.index - 1
		if (endIndex > startIndex) {
			buffer.append(source.buffer, startIndex, endIndex - startIndex)
		}

		return buffer.toString()
	}


	private fun readToken(required: JsonToken) {
		val token = nextToken
		if (token != required) {
			unexpectedTokenError(
				actual = token,
				expected = "'$required'",
				offset = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1
	}


	@Suppress("SameParameterValue")
	private fun <Token : JsonToken?> readToken(required: Token, alternative: Token): Token {
		val token = nextToken
		if (token != required && token != alternative) {
			unexpectedTokenError(
				actual = token,
				expected = "'$required' or '$alternative'",
				offset = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1

		@Suppress("UNCHECKED_CAST")
		return token as Token
	}


	private inline fun <Value> readValue(checkBoundary: Boolean, crossinline read: () -> Value): Value {
		willReadValue()

		val value = read()

		if (checkBoundary) {
			val nextCharacter = source.peekCharacter()
			if (!JsonCharacter.isValueBoundary(nextCharacter)) {
				unexpectedCharacterError(actual = nextCharacter, expected = "end of value")
			}
		}

		didReadValue()

		return value
	}


	override fun terminate() {
		ensureNotClosed()
		val nextToken = nextToken

		close()

		if (nextToken != null)
			throw JsonException.Syntax(
				message = "expected end of input but found token $nextToken",
				offset = source.sourceIndex,
				path = JsonPath.root
			)
	}


	private fun unexpectedCharacterError(actual: Int, expected: String, offset: Int = source.sourceIndex): Nothing =
		throw if (offset == 0 && actual == JsonCharacter.end)
			JsonException.Syntax(
				message = "Cannot parse empty JSON data",
				offset = 0,
				path = JsonPath.root
			)
		else
			JsonException.Syntax(
				message = "unexpected ${JsonCharacter.toString(actual)}, expected $expected",
				offset = offset,
				path = path
			)


	private fun unexpectedTokenError(actual: JsonToken?, expected: String, offset: Int = source.sourceIndex): Nothing {
		throw JsonException.Syntax(
			message = "Unexpected $actual, expected $expected",
			offset = offset,
			path = path
		)
	}


	private inline fun valueIsolationCheck(value: Boolean, lazyMessage: () -> String) {
		// contract {
		//  returns() implies value
		// }

		if (!value) valueIsolationError(lazyMessage())
	}


	private fun valueIsolationError(message: String): Nothing {
		throw JsonException.Parsing(
			message = "Value isolation failed: $message",
			offset = offset,
			path = path
		)
	}


	private fun willReadValue() {
		valueIsolationCheck(!state.isInValueIsolation || !state.hasReadIsolatedValue) { "cannot read more than one value in a context where only one is expected" }
	}


	private fun TextInput.readCharacter(required: Int) =
		readCharacter(required) { JsonCharacter.toString(required) }


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
			unexpectedCharacterError(
				actual = character,
				expected = expected(),
				offset = index - 1
			)
		}

		return character
	}


	private class State {

		var currentValueListIndex = -1
		var currentValueMapKey: String? = null
		var hasReadIsolatedValue = false
		var tokenLocation = TokenLocation.beforeRootValue
		var valueIsolationCount = 0


		val isInValueIsolation
			get() = valueIsolationCount > 0


		fun reset(tokenLocation: TokenLocation) {
			this.tokenLocation = tokenLocation

			currentValueListIndex = -1
			currentValueMapKey = null
			hasReadIsolatedValue = false
			valueIsolationCount = 0
		}


		fun toPathElement(): JsonPath.Element? {
			if (currentValueListIndex >= 0) return JsonPath.Element.ListIndex(currentValueListIndex)
			currentValueMapKey?.let { return JsonPath.Element.MapKey(it) }

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
