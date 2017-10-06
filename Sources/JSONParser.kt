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


	private class Run(
		private val inputString: String
	) {

		private var inputIndex = 0
		private val inputLength = inputString.length
		private val reusableBuilder = StringBuilder()

		val root = parse()


		private fun consumeCharacter(): Char {
			val index = inputIndex
			assert(index < inputLength) // all calls are already checked

			++inputIndex

			return inputString[index]
		}


		private fun consumeCharacter(expectedCharacter: Char) {
			val index = inputIndex
			if (index >= inputLength) {
				throw JSONPathBuildingException("unexpected end of input string, expected '$expectedCharacter'")
			}

			++inputIndex

			val character = inputString[index]
			if (character != expectedCharacter) {
				throw JSONPathBuildingException("unexpected character '$character' at index ${inputIndex - 1}, expected '$expectedCharacter'")
			}
		}


		private fun internalParseList(): List<*> {
			skipWhitespaces()
			consumeCharacter('[')

			val list = mutableListOf<Any?>()

			while (true) {
				skipWhitespaces()

				if (peekCharacter() == ']') {
					consumeCharacter()
					return list
				}

				if (list.isNotEmpty()) {
					val character = consumeCharacter()
					if (character != ',') {
						throw JSONPathBuildingException("unexpected character '$character' at index $inputIndex, expected ']' or ','")
					}

					skipWhitespaces()
				}

				list += internalParseValue()
			}
		}


		private fun internalParseMap(): Map<String, *> {
			skipWhitespaces()
			consumeCharacter('{')

			val map = mutableMapOf<String, Any?>()

			while (true) {
				skipWhitespaces()

				var character = peekCharacter()
				if (character == '}') {
					consumeCharacter()
					return map
				}

				if (map.isNotEmpty()) {
					if (character != ',') {
						throw JSONPathBuildingException("unexpected character '$character' at index $inputIndex, expected '}' or ','")
					}

					consumeCharacter()
					skipWhitespaces()
					character = peekCharacter()
				}

				if (character != '"') {
					throw JSONPathBuildingException("unexpected character '$character' at index $inputIndex, expected '}' or '\"'")
				}

				val key = internalParseString()
				skipWhitespaces()
				consumeCharacter(':')
				map[key] = internalParseValue()
			}
		}


		private fun internalParseNumber(): Number {
			skipWhitespaces()
			peekCharacter()

			var state = NumberParserState.start

			val startIndex = inputIndex
			var mustParseFloatingPoint = false

			var index = startIndex
			loop@ while (index < inputLength) {
				val character = inputString[index]
				state = when (state) {
					NumberParserState.start -> {
						when (character) {
							'0' -> NumberParserState.afterZeroInteger
							'1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterInteger
							'-' -> NumberParserState.afterSign
							else -> throw JSONPathBuildingException("unexpected character '$character' in number at index $index, expected '+', '-' or a digit")
						}
					}

					NumberParserState.afterSign -> {
						when (character) {
							'0' -> NumberParserState.afterZeroInteger
							'1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterInteger
							else -> throw JSONPathBuildingException("unexpected character '$character' in number at index $index, expected a digit")
						}
					}

					NumberParserState.afterZeroInteger -> {
						when (character) {
							'.' -> NumberParserState.afterDecimalSeparator
							'e', 'E' -> NumberParserState.afterExponentSeparator
							else -> null
						}
					}

					NumberParserState.afterInteger -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterInteger
							'.' -> NumberParserState.afterDecimalSeparator
							'e', 'E' -> NumberParserState.afterExponentSeparator
							else -> null
						}
					}

					NumberParserState.afterDecimalSeparator -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterFractional
							else -> throw JSONPathBuildingException("unexpected character '$character' in number at index $index, expected a digit")
						}
					}

					NumberParserState.afterFractional -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterFractional
							'e', 'E' -> NumberParserState.afterExponentSeparator
							else -> null
						}
					}

					NumberParserState.afterExponentSeparator -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterExponent
							'+', '-' -> NumberParserState.afterExponentSign
							else -> throw JSONPathBuildingException("unexpected character '$character' in number at index $index, expected '+', '-' or a digit")
						}
					}

					NumberParserState.afterExponentSign -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterExponent
							else -> throw JSONPathBuildingException("unexpected character '$character' in number at index $index, expected a digit")
						}
					}

					NumberParserState.afterExponent -> {
						when (character) {
							'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NumberParserState.afterExponent
							else -> null
						}
					}
				} ?: break

				if (state == NumberParserState.afterDecimalSeparator) {
					mustParseFloatingPoint = true
				}

				index += 1
			}

			val endIndex = index
			inputIndex = endIndex

			when (state) {
				NumberParserState.afterZeroInteger ->
					return 0

				NumberParserState.start,
				NumberParserState.afterSign,
				NumberParserState.afterDecimalSeparator,
				NumberParserState.afterExponentSeparator,
				NumberParserState.afterExponentSign ->
					throw JSONPathBuildingException("unexpected end of input string when parsing number at index $startIndex")

				NumberParserState.afterInteger,
				NumberParserState.afterFractional,
				NumberParserState.afterExponent ->
					Unit
			}

			val substring = inputString.substring(startIndex, endIndex)

			if (!mustParseFloatingPoint) {
				val value = substring.toLongOrNull()
				if (value != null) {
					return if (value in Int.MIN_VALUE .. Int.MAX_VALUE) value.toInt() else value
				}
			}

			return substring.toDouble()
		}


		private fun internalParseString(): String {
			skipWhitespaces()
			consumeCharacter('"')

			val beginIndex = inputIndex
			var escapedStringLength = 0
			var containsEscapes = false

			var character = 0.toChar()
			var index = inputIndex
			loop@ while (index < inputLength) {
				character = inputString[index]
				when (character) {
					'"' -> break@loop

					'\\' -> {
						containsEscapes = true

						++escapedStringLength
						++index
					}

					'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
					'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
					'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
					'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t' ->
						throw JSONPathBuildingException("unescaped control character in string at index $index")
				}

				++escapedStringLength
				++index
			}

			if (character != '"') {
				throw JSONPathBuildingException("unterminated string value")
			}

			val endIndex = index
			inputIndex = index + 1

			if (escapedStringLength == 0) {
				return ""
			}

			if (!containsEscapes) {
				return inputString.substring(beginIndex, endIndex)
			}

			val builder = reusableBuilder
			builder.setLength(0)
			builder.ensureCapacity(escapedStringLength)

			index = beginIndex
			while (index < endIndex) {
				character = inputString[index]
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
								val sequenceBeginIndex = index + 1
								val sequenceEndIndex = sequenceBeginIndex + 4
								if (sequenceEndIndex > inputLength) {
									throw JSONPathBuildingException("unexpected end of Unicode escape sequence at index $sequenceBeginIndex")
								}

								val sequence = inputString.substring(sequenceBeginIndex, sequenceEndIndex)
								character = sequence.toIntOrNull(16)?.toChar()
									?: throw JSONPathBuildingException("invalid unicode escape sequence '$sequence' at index $sequenceBeginIndex")

								builder.append(character)
								index += 4
							}
							else -> throw JSONPathBuildingException("unknown escape sequence character '$character' at index $index")
						}
					}
					else -> builder.append(character)
				}

				index += 1
			}

			return builder.toString()
		}


		private fun internalParseValue(): Any? {
			skipWhitespaces()

			val character = peekCharacter()
			return when (character) {
				'"' -> internalParseString()

				'+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> internalParseNumber()

				'{' -> internalParseMap()

				'[' -> internalParseList()

				't' -> {
					consumeCharacter()
					consumeCharacter('r')
					consumeCharacter('u')
					consumeCharacter('e')

					true
				}

				'f' -> {
					consumeCharacter()
					consumeCharacter('a')
					consumeCharacter('l')
					consumeCharacter('s')
					consumeCharacter('e')

					false
				}

				'n' -> {
					consumeCharacter()
					consumeCharacter('u')
					consumeCharacter('l')
					consumeCharacter('l')

					null
				}

				else ->
					throw JSONPathBuildingException("unexpected character '$character' at index $inputIndex")
			}
		}


		private fun parse(): Any? {
			val root = internalParseValue()
			skipWhitespaces()

			if (inputIndex < inputLength) {
				throw JSONPathBuildingException("unexpected extra data at index $inputIndex")
			}

			return root
		}


		private fun peekCharacter(): Char {
			val index = inputIndex
			if (index >= inputLength) {
				throw JSONPathBuildingException("unexpected end of input string")
			}

			return inputString[index]
		}


		private fun skipWhitespaces() {
			var index = inputIndex
			loop@ while (index < inputLength) {
				when (inputString[index]) {
					' ', '\n', '\r', '\t' ->
						Unit

					else ->
						break@loop
				}
				++index
			}

			inputIndex = index
		}


		private enum class NumberParserState {
			start,
			afterSign,
			afterZeroInteger,
			afterInteger,
			afterDecimalSeparator,
			afterFractional,
			afterExponentSeparator,
			afterExponentSign,
			afterExponent
		}
	}
}
