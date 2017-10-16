package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API
import java.io.Reader


// FIXME rename
// FIXME max nesting, max string length, max number length
@API(status = API.Status.EXPERIMENTAL)
internal class JSONStreamReader(reader: Reader) : JSONReader {

	private val buffer = StringBuilder()
	private val input = TextInput(reader = reader)
	private var peekedToken: JSONToken? = null
	private var peekedTokenIndex = -1
	private var state = State.initial
	private val stateStack = mutableListOf<State>()


	private fun expectEndOfValue() {
		val nextCharacter = input.peekCharacter()
		if (!Character.isValueBoundary(nextCharacter)) {
			throw unexpectedCharacter(
				nextCharacter,
				expected = "end of value",
				characterIndex = input.index
			)
		}
	}


	override val nextToken: JSONToken?
		get() {
			if (peekedTokenIndex >= 0) {
				return peekedToken
			}

			val peekedToken = peekToken()
			this.peekedToken = peekedToken
			this.peekedTokenIndex = input.index

			return peekedToken
		}


	private fun peekToken(): JSONToken? {
		val input = input

		while (true) {
			input.skipWhitespaceCharacters()

			val character = input.peekCharacter()
			when (state) {
				State.afterListElement ->
					when (character) {
						Character.Symbol.comma -> {
							state = State.afterListElementSeparator
							input.readCharacter()
						}

						Character.Symbol.rightSquareBracket -> {
							restoreState()
							return JSONToken.listEnd
						}

						else -> throw unexpectedCharacter(character, expected = "',' or ']'")
					}

				State.afterListElementSeparator -> {
					state = State.afterListElement
					return peekValueToken(expected = "a value")
				}

				State.afterListStart ->
					when (character) {
						Character.Symbol.rightSquareBracket -> {
							restoreState()
							return JSONToken.listEnd
						}
						else -> {
							state = State.afterListElement
							return peekValueToken(expected = "a value or ']'")
						}
					}

				State.afterMapElement ->
					when (character) {
						Character.Symbol.comma -> {
							state = State.afterMapElementSeparator
							input.readCharacter()
						}

						Character.Symbol.rightCurlyBracket -> {
							restoreState()
							return JSONToken.mapEnd
						}

						else -> throw unexpectedCharacter(character, expected = "',' or '}'")
					}

				State.afterMapElementSeparator ->
					when (character) {
						Character.Symbol.quotationMark -> {
							state = State.afterMapKey
							return JSONToken.mapKey
						}

						else -> throw unexpectedCharacter(character, expected = "'\"'")
					}

				State.afterMapKey ->
					when (character) {
						Character.Symbol.colon -> {
							state = State.afterMapKeySeparator
							input.readCharacter()
						}

						else -> throw unexpectedCharacter(character, expected = "':'")
					}

				State.afterMapKeySeparator -> {
					state = State.afterMapElement
					return peekValueToken(expected = "a value")
				}

				State.afterMapStart ->
					when (character) {
						Character.Symbol.quotationMark -> {
							state = State.afterMapKey
							return JSONToken.mapKey
						}

						Character.Symbol.rightCurlyBracket -> {
							restoreState()
							return JSONToken.mapEnd
						}

						else -> throw unexpectedCharacter(character, expected = "'\"' or '}'")
					}

				State.end ->
					when (character) {
						Character.end ->
							return null

						else -> throw unexpectedCharacter(character, expected = "end of input")
					}

				State.initial -> {
					state = State.end
					return peekValueToken(expected = "a value")
				}
			}
		}
	}


	private fun peekValueToken(expected: String): JSONToken? {
		val character = input.peekCharacter()
		return when (character) {
			Character.Symbol.quotationMark ->
				JSONToken.stringValue

			Character.Symbol.hyphenMinus,
			Character.Digit.zero,
			Character.Digit.one,
			Character.Digit.two,
			Character.Digit.three,
			Character.Digit.four,
			Character.Digit.five,
			Character.Digit.six,
			Character.Digit.seven,
			Character.Digit.eight,
			Character.Digit.nine ->
				JSONToken.numberValue

			Character.Letter.f,
			Character.Letter.t ->
				JSONToken.booleanValue

			Character.Letter.n ->
				JSONToken.nullValue

			Character.Symbol.leftCurlyBracket -> {
				saveState()
				state = State.afterMapStart

				JSONToken.mapStart
			}

			Character.Symbol.leftSquareBracket -> {
				saveState()
				state = State.afterListStart

				JSONToken.listStart
			}

			else -> throw unexpectedCharacter(character, expected = expected)
		}
	}


	override fun readBoolean(): Boolean {
		readToken(JSONToken.booleanValue)

		val input = input
		if (input.peekCharacter() == Character.Letter.t) {
			input.readCharacter(Character.Letter.t)
			input.readCharacter(Character.Letter.r)
			input.readCharacter(Character.Letter.u)
			input.readCharacter(Character.Letter.e)
			expectEndOfValue()

			return true
		}
		else {
			input.readCharacter(Character.Letter.f)
			input.readCharacter(Character.Letter.a)
			input.readCharacter(Character.Letter.l)
			input.readCharacter(Character.Letter.s)
			input.readCharacter(Character.Letter.e)
			expectEndOfValue()

			return false
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

		input.readCharacter(Character.Symbol.rightSquareBracket)
	}


	override fun readListStart() {
		readToken(JSONToken.listStart)

		input.readCharacter(Character.Symbol.leftSquareBracket)
	}


	override fun readLong(): Long {
		readToken(JSONToken.numberValue)

		val isNegative: Boolean
		val negativeLimit: Long

		val input = input
		var character = input.readCharacter()
		if (character == Character.Symbol.hyphenMinus) {
			isNegative = true
			character = input.readCharacter(required = Character::isDigit) { "a digit" }
			negativeLimit = Long.MIN_VALUE
		}
		else {
			isNegative = false
			negativeLimit = -Long.MAX_VALUE
		}

		val minimumBeforeMultiplication = negativeLimit / 10
		var value = 0L

		if (character == Character.Digit.zero) {
			character = input.readCharacter(required = { !Character.isDigit(it) }) {
				Character.toString(
					Character.Symbol.fullStop,
					Character.Letter.e,
					Character.Letter.E
				) + " or end of number after a leading '0'"
			}
		}
		else {
			do {
				val digit = character - Character.Digit.zero
				if (value < minimumBeforeMultiplication) {
					value = negativeLimit

					do character = input.readCharacter()
					while (Character.isDigit(character))
					break
				}

				value *= 10

				if (value < negativeLimit + digit) {
					value = negativeLimit

					do character = input.readCharacter()
					while (Character.isDigit(character))
					break
				}

				value -= digit
				character = input.readCharacter()
			}
			while (Character.isDigit(character))

			if (!isNegative) {
				value *= -1
			}
		}

		if (character == Character.Symbol.fullStop) { // truncate decimal value
			input.readCharacter(required = Character::isDigit) { "a digit in decimal value of number" }

			do character = input.readCharacter()
			while (Character.isDigit(character))
		}

		if (character == Character.Letter.e || character == Character.Letter.E) { // apply exponent
			val exponentIsNegative: Boolean

			when (input.peekCharacter()) {
				Character.Symbol.plusSign -> {
					exponentIsNegative = false
					input.readCharacter()
				}

				Character.Symbol.hyphenMinus -> {
					exponentIsNegative = true
					input.readCharacter()
				}

				else ->
					exponentIsNegative = false
			}

			character = input.readCharacter(required = Character::isDigit) { "a digit in exponent value of number" }

			var exponent = 0
			do {
				val digit = character - Character.Digit.zero
				exponent = (exponent * 10) + digit

				if (exponent >= 19) {
					do character = input.readCharacter()
					while (Character.isDigit(character))
					break
				}

				character = input.readCharacter()
			}
			while (Character.isDigit(character))

			if (exponent >= 19) {
				if (exponentIsNegative) {
					return 0L
				}
				else {
					return Long.MAX_VALUE
				}
			}

			val multiplier = exponentMultipliers[exponent]

			value = when {
				exponentIsNegative -> value / multiplier
				Long.canMultiplyWithoutOverflow(value, multiplier) -> value * multiplier
				isNegative -> Long.MIN_VALUE
				else -> Long.MAX_VALUE
			}
		}

		input.seekBackOneCharacter()
		expectEndOfValue()

		return value
	}

	override fun readMapEnd() {
		readToken(JSONToken.mapEnd)

		input.readCharacter(Character.Symbol.rightCurlyBracket)
	}


	override fun readMapStart() {
		readToken(JSONToken.mapStart)

		input.readCharacter(Character.Symbol.leftCurlyBracket)
	}


	override fun readNull(): Nothing? {
		readToken(JSONToken.nullValue)

		val input = input
		input.readCharacter(Character.Letter.n)
		input.readCharacter(Character.Letter.u)
		input.readCharacter(Character.Letter.l)
		input.readCharacter(Character.Letter.l)
		expectEndOfValue()

		return null
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

		val buffer = buffer
		buffer.setLength(0)

		var shouldParseAsFloatingPoint = false
		val input = input
		var character = input.readCharacter()

		// consume optional minus sign
		if (character == Character.Symbol.hyphenMinus) {
			buffer.append('-')
			character = input.readCharacter()
		}

		// consume integer value
		when (character) {
			Character.Digit.zero -> {
				buffer.append('0')
				character = input.readCharacter(required = { !Character.isDigit(it) }) {
					Character.toString(
						Character.Symbol.fullStop,
						Character.Letter.e,
						Character.Letter.E
					) + " or end of number after a leading '0'"
				}
			}

			Character.Digit.one,
			Character.Digit.two,
			Character.Digit.three,
			Character.Digit.four,
			Character.Digit.five,
			Character.Digit.six,
			Character.Digit.seven,
			Character.Digit.eight,
			Character.Digit.nine ->
				do {
					buffer.append(character.toChar())
					character = input.readCharacter()
				}
				while (Character.isDigit(character))

			else ->
				throw unexpectedCharacter(
					character,
					expected = "a digit in integer value of number",
					characterIndex = input.index - 1
				)
		}

		// consume optional decimal separator and value
		if (character == Character.Symbol.fullStop) {
			shouldParseAsFloatingPoint = true

			buffer.append('.')
			character = input.readCharacter(required = Character::isDigit) { "a digit in decimal value of number" }

			do {
				buffer.append(character.toChar())
				character = input.readCharacter()
			}
			while (Character.isDigit(character))
		}

		// consume optional exponent separator and value
		if (character == Character.Letter.e || character == Character.Letter.E) {
			shouldParseAsFloatingPoint = true

			buffer.append(character.toChar())

			character = input.peekCharacter()
			if (character == Character.Symbol.plusSign || character == Character.Symbol.hyphenMinus) {
				buffer.append(character.toChar())
				input.readCharacter()
			}

			character = input.readCharacter(required = Character::isDigit) { "a digit in exponent value of number" }

			do {
				buffer.append(character.toChar())
				character = input.readCharacter()
			}
			while (Character.isDigit(character))
		}

		input.seekBackOneCharacter()
		expectEndOfValue()

		return shouldParseAsFloatingPoint
	}


	override fun readString(): String {
		readToken(JSONToken.stringValue, JSONToken.mapKey)

		val buffer = buffer
		buffer.setLength(0)

		val input = input
		input.readCharacter(Character.Symbol.quotationMark)

		do {
			var character = input.readCharacter()
			when (character) {
				Character.Symbol.reverseSolidus -> {
					character = input.readCharacter()
					when (character) {
						Character.Symbol.reverseSolidus,
						Character.Symbol.solidus ->
							buffer.append(character.toChar())

						Character.Symbol.quotationMark -> {
							buffer.append(character.toChar())
							character = 0
						}

						Character.Letter.b -> buffer.append('\b')
						Character.Letter.f -> buffer.append('\u000C')
						Character.Letter.n -> buffer.append('\n')
						Character.Letter.r -> buffer.append('\r')
						Character.Letter.t -> buffer.append('\t')
						Character.Letter.u -> {
							val digit1 = input.readCharacter(required = Character::isHexDigit) { "0-9, a-f or A-F" }
							val digit2 = input.readCharacter(required = Character::isHexDigit) { "0-9, a-f or A-F" }
							val digit3 = input.readCharacter(required = Character::isHexDigit) { "0-9, a-f or A-F" }
							val digit4 = input.readCharacter(required = Character::isHexDigit) { "0-9, a-f or A-F" }

							character = (Character.parseHexDigit(digit1) shl 12) or
								(Character.parseHexDigit(digit2) shl 8) or
								(Character.parseHexDigit(digit3) shl 4) or
								Character.parseHexDigit(digit4)

							buffer.append(character.toChar())
						}
						else -> throw unexpectedCharacter(character, "an escape sequence starting with '\\', '/', '\"', 'b', 'f', 'n', 'r', 't' or 'u'")
					}
				}

				0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F ->
					throw unexpectedCharacter(character, "an escape sequence")

				Character.Symbol.quotationMark ->
					Unit

				Character.end ->
					throw JSONException("unterminated string value")

				else ->
					buffer.append(character.toChar())
			}
		}
		while (character != Character.Symbol.quotationMark)

		return buffer.toString()
	}


	private fun readToken(required: JSONToken) {
		val token = nextToken
		if (token != required) {
			throw JSONException.unexpectedToken(
				token,
				expected = "'$required'",
				characterIndex = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1
	}


	private fun readToken(required: JSONToken, alternative: JSONToken) {
		val token = nextToken
		if (token != required && token != alternative) {
			throw JSONException.unexpectedToken(
				token,
				expected = "'$required' or '$alternative'",
				characterIndex = peekedTokenIndex
			)
		}

		peekedToken = null
		peekedTokenIndex = -1
	}


	private fun restoreState() {
		state = stateStack.removeAt(stateStack.size - 1)
	}


	private fun saveState() {
		stateStack.add(state)
	}


	private fun unexpectedCharacter(character: Int, expected: String, characterIndex: Int = input.index) =
		JSONException.unexpectedCharacter(character, expected = expected, characterIndex = characterIndex)


	private companion object {

		private val exponentMultipliers = longArrayOf(
			1L,
			10L,
			100L,
			1000L,
			10000L,
			100000L,
			1000000L,
			10000000L,
			100000000L,
			1000000000L,
			10000000000L,
			100000000000L,
			1000000000000L,
			10000000000000L,
			100000000000000L,
			1000000000000000L,
			10000000000000000L,
			100000000000000000L,
			1000000000000000000L
		)
	}


	private enum class State {

		afterListElementSeparator,
		afterListElement,
		afterListStart,
		afterMapElement,
		afterMapElementSeparator,
		afterMapKey,
		afterMapKeySeparator,
		afterMapStart,
		end,
		initial,
	}
}
