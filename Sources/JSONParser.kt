package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
class JSONParser {

	@API(status = API.Status.EXPERIMENTAL)
	fun parse(string: String) =
		JSONPathBuildingException.track {
			Run(string).root
		}


	@API(status = API.Status.EXPERIMENTAL)
	fun parseList(string: String) =
		JSONPathBuildingException.track {
			Run(string).root as? List<*> ?: throw JSONPathBuildingException("expected a list")
		}


	@API(status = API.Status.EXPERIMENTAL)
	fun parseMap(string: String) =
		JSONPathBuildingException.track {
			@Suppress("UNCHECKED_CAST")
			Run(string).root as? Map<String, *> ?: throw JSONPathBuildingException("expected a map")
		}


	private object Character {

		object Digit {

			const val one = '1'.toInt()
			const val two = '2'.toInt()
			const val three = '3'.toInt()
			const val four = '4'.toInt()
			const val five = '5'.toInt()
			const val six = '6'.toInt()
			const val seven = '7'.toInt()
			const val eight = '8'.toInt()
			const val nine = '9'.toInt()
			const val zero = '0'.toInt()
		}

		object Letter {

			const val a = 'a'.toInt()
			const val e = 'e'.toInt()
			const val E = 'E'.toInt()
			const val f = 'f'.toInt()
			const val l = 'l'.toInt()
			const val n = 'n'.toInt()
			const val r = 'r'.toInt()
			const val s = 's'.toInt()
			const val t = 't'.toInt()
			const val u = 'u'.toInt()
		}

		object Symbol {

			const val colon = ':'.toInt()
			const val comma = ','.toInt()
			const val fullStop = '.'.toInt()
			const val hyphenMinus = '-'.toInt()
			const val leftCurlyBracket = '{'.toInt()
			const val leftSquareBracket = '['.toInt()
			const val plusSign = '+'.toInt()
			const val quotationMark = '"'.toInt()
			const val reverseSolidus = '\\'.toInt()
			const val rightCurlyBracket = '}'.toInt()
			const val rightSquareBracket = ']'.toInt()
		}

		const val end = -1


		fun isDigit(character: Int) =
			character in Digit.zero .. Digit.nine


		fun toString(character: Int) =
			if (character == end)
				"end of input"
			else
				"'" + java.lang.Character.toString(character.toChar()) + "'"


		fun toString(vararg characters: Int) =
			characters.joinToString(" or ") { toString(it) }
	}


	private class Run(
		private val inputString: String
	) {

		private var inputIndex = 0
		private val inputLength = inputString.length
		private val reusableBuilder = StringBuilder()

		val root = parse()


		private fun consumeCharacter(): Int {
			val index = inputIndex
			this.inputIndex += 1

			if (index >= inputLength) {
				return Character.end
			}

			return inputString[index].toInt()
		}


		private fun consumeCharacter(expected: Int) =
			consumeCharacter(expected) { Character.toString(expected) }


		private inline fun consumeCharacter(expected: Int, accepted: () -> String): Int =
			consumeCharacter(expected = { it == expected }, accepted = accepted)


		private inline fun consumeCharacter(expected: (character: Int) -> Boolean, accepted: () -> String): Int {
			val index = inputIndex

			val character = consumeCharacter()
			if (!expected(character)) {
				throw unexpectedCharacter(
					character,
					accepted = accepted(),
					index = index
				)
			}

			return character
		}


		private fun consumeWhitespaces() {
			val length = inputLength
			val string = inputString

			var index = inputIndex
			loop@ while (index < length) {
				when (string[index]) {
					' ', '\n', '\r', '\t' -> Unit
					else -> break@loop
				}

				index += 1
			}

			inputIndex = index
		}


		private fun internalParseList(): List<*> {
			consumeWhitespaces()
			consumeCharacter(Character.Symbol.leftSquareBracket)
			consumeWhitespaces()

			val list = mutableListOf<Any?>()

			while (peekCharacter() != Character.Symbol.rightSquareBracket) {
				if (list.isNotEmpty()) {
					consumeCharacter(Character.Symbol.comma) {
						Character.toString(
							Character.Symbol.comma,
							Character.Symbol.rightSquareBracket
						)
					}
				}

				list += internalParseValue()
				consumeWhitespaces()
			}

			consumeCharacter()
			return list
		}


		private fun internalParseMap(): Map<String, *> {
			consumeWhitespaces()
			consumeCharacter(Character.Symbol.leftCurlyBracket)
			consumeWhitespaces()

			val map = mutableMapOf<String, Any?>()

			while (peekCharacter() != Character.Symbol.rightCurlyBracket) {
				if (map.isNotEmpty()) {
					consumeCharacter(Character.Symbol.comma) {
						Character.toString(
							Character.Symbol.comma,
							Character.Symbol.rightCurlyBracket
						)
					}
				}

				val key = internalParseString()

				consumeWhitespaces()
				consumeCharacter(Character.Symbol.colon)

				map[key] = internalParseValue()
				consumeWhitespaces()
			}

			consumeCharacter()
			return map
		}


		private fun internalParseNumber(): Number {
			consumeWhitespaces()

			val startIndex = inputIndex
			var mustParseAsDouble = false

			var character = consumeCharacter()

			// consume optional minus sign
			if (character == Character.Symbol.hyphenMinus) {
				character = consumeCharacter()
			}

			// consume integer value
			when (character) {
				Character.Digit.zero -> {
					character = consumeCharacter(expected = { !Character.isDigit(it) }) {
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
					do character = consumeCharacter()
					while (Character.isDigit(character))

				else ->
					throw unexpectedCharacter(
						character,
						accepted = "a digit in integer value of number",
						index = inputIndex - 1
					)
			}

			// consume optional decimal separator and value
			if (character == Character.Symbol.fullStop) {
				mustParseAsDouble = true

				consumeCharacter(expected = Character::isDigit) { "a digit in decimal value of number" }

				do character = consumeCharacter()
				while (Character.isDigit(character))
			}

			// consume optional exponent separator and value
			if (character == Character.Letter.e || character == Character.Letter.E) {
				mustParseAsDouble = true

				character = peekCharacter()
				if (character == Character.Symbol.plusSign || character == Character.Symbol.hyphenMinus) {
					consumeCharacter()
				}

				consumeCharacter(expected = Character::isDigit) { "a digit in exponent value of number" }

				do character = consumeCharacter()
				while (Character.isDigit(character))
			}

			seekBackOneCharacter()

			val endIndex = inputIndex
			val substring = inputString.substring(startIndex, endIndex)

			if (!mustParseAsDouble) {
				val value = substring.toLongOrNull()
				if (value != null) {
					return if (value in Int.MIN_VALUE .. Int.MAX_VALUE) value.toInt() else value
				}
			}

			return substring.toDouble()
		}


		private fun internalParseString(): String {
			consumeWhitespaces()
			consumeCharacter(Character.Symbol.quotationMark)

			val startIndex = inputIndex
			var containsEscapes = false

			var inputCharacter = consumeCharacter()
			loop@ while (inputCharacter != Character.Symbol.quotationMark) {
				when (inputCharacter) {
					Character.end ->
						throw JSONPathBuildingException("unterminated string value")

					Character.Symbol.reverseSolidus -> {
						containsEscapes = true
						consumeCharacter()
					}

					0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
					0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F ->
						throw JSONPathBuildingException("unescaped control character in string", index = inputIndex - 1)
				}

				inputCharacter = consumeCharacter()
			}

			val endIndex = inputIndex - 1
			if (startIndex >= endIndex) {
				return ""
			}

			return if (containsEscapes) {
				internalParseString(startIndex, endIndex)
			}
			else {
				inputString.substring(startIndex, endIndex)
			}
		}


		private fun internalParseString(startIndex: Int, endIndex: Int): String {
			val inputString = inputString

			val builder = reusableBuilder
			builder.setLength(0)
			builder.ensureCapacity(endIndex - startIndex - 1)

			var index = startIndex
			while (index < endIndex) {
				var character = inputString[index]
				when (character) {
					'\\' -> {
						index += 1

						character = inputString[index]
						when (character) {
							'"', '\\', '/' -> builder.append(character)
							'b' -> builder.append('\b')
							'f' -> builder.append('\u000C')
							'n' -> builder.append('\n')
							'r' -> builder.append('\r')
							't' -> builder.append('\t')
							'u' -> {
								val sequenceStartIndex = index + 1
								val sequenceEndIndex = sequenceStartIndex + 4
								if (sequenceEndIndex > endIndex) {
									throw JSONPathBuildingException("unexpected end of Unicode escape sequence", index = sequenceStartIndex)
								}

								val sequence = inputString.substring(sequenceStartIndex, sequenceEndIndex)
								character = sequence.toIntOrNull(16)?.toChar()
									?: throw JSONPathBuildingException("invalid unicode escape sequence '$sequence'", index = sequenceStartIndex)

								builder.append(character)
								index += 4
							}
							else -> throw JSONPathBuildingException("unknown escape sequence character '$character'", index = index)
						}
					}
					else -> builder.append(character)
				}

				index += 1
			}

			return builder.toString()
		}


		private fun internalParseValue(): Any? {
			consumeWhitespaces()

			val character = peekCharacter()
			return when (character) {
				Character.Symbol.quotationMark ->
					internalParseString()

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
					internalParseNumber()

				Character.Symbol.leftCurlyBracket ->
					internalParseMap()

				Character.Symbol.leftSquareBracket ->
					internalParseList()

				Character.Letter.t -> {
					consumeCharacter()
					consumeCharacter(Character.Letter.r)
					consumeCharacter(Character.Letter.u)
					consumeCharacter(Character.Letter.e)

					true
				}

				Character.Letter.f -> {
					consumeCharacter()
					consumeCharacter(Character.Letter.a)
					consumeCharacter(Character.Letter.l)
					consumeCharacter(Character.Letter.s)
					consumeCharacter(Character.Letter.e)

					false
				}

				Character.Letter.n -> {
					consumeCharacter()
					consumeCharacter(Character.Letter.u)
					consumeCharacter(Character.Letter.l)
					consumeCharacter(Character.Letter.l)

					null
				}

				else ->
					throw unexpectedCharacter(
						character,
						accepted = "a value",
						index = inputIndex
					)
			}
		}


		private fun parse(): Any? {
			val root = internalParseValue()
			consumeWhitespaces()

			if (inputIndex < inputLength) {
				throw JSONPathBuildingException("unexpected extra data", index = inputIndex)
			}

			return root
		}


		private fun peekCharacter(): Int {
			val inputIndex = inputIndex
			val inputCharacter = consumeCharacter()
			this.inputIndex = inputIndex

			return inputCharacter
		}


		private fun seekBackOneCharacter() {
			assert(inputIndex >= 1)
			inputIndex -= 1
		}


		private companion object {

			fun unexpectedCharacter(character: Int, accepted: String, index: Int): Exception =
				JSONPathBuildingException("unexpected ${Character.toString(character)} expected $accepted", index = index)
		}
	}
}
