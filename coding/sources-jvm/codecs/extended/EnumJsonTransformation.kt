package io.fluidsonic.json

import io.fluidsonic.json.EnumJsonTransformation.*


public sealed class EnumJsonTransformation {

	public class Name(public val case: Case? = null) : EnumJsonTransformation()
	public object Ordinal : EnumJsonTransformation()
	public class ToString(public val case: Case? = null) : EnumJsonTransformation()


	@Suppress("EnumEntryName")
	public enum class Case {

		lowerCamelCase,
		`lower-kebab-case`,
		lower_snake_case,
		lowercase,
		lowercase_words, // cannot use space here because Android DEX doesn't support it
		UpperCamelCase,
		`UPPER-KEBAB-CASE`,
		UPPER_SNAKE_CASE,
		UPPERCASE,
		UPPERCASE_WORDS // cannot use space here because Android DEX doesn't support it
	}
}


private fun wordToLowerCamelCase(index: Int, word: String) =
	if (index == 0)
		word.lowercase()
	else
		word.camelize()


internal fun Case?.convert(string: String) =
	when (this) {
		null -> string
		Case.lowerCamelCase -> string.words().mapIndexed(::wordToLowerCamelCase).joinToString(separator = "")
		Case.`lower-kebab-case` -> string.words().joinToString(separator = "-").lowercase()
		Case.lower_snake_case -> string.words().joinToString(separator = "_").lowercase()
		Case.lowercase -> string.lowercase()
		Case.lowercase_words -> string.words().joinToString(separator = " ").lowercase()
		Case.UpperCamelCase -> string.words().joinToString(separator = "") { it.camelize() }
		Case.`UPPER-KEBAB-CASE` -> string.words().joinToString(separator = "-").uppercase()
		Case.UPPER_SNAKE_CASE -> string.words().joinToString(separator = "_").uppercase()
		Case.UPPERCASE -> string.uppercase()
		Case.UPPERCASE_WORDS -> string.words().joinToString(separator = " ").uppercase()
	}


private fun String.camelize() =
	if (isNotEmpty())
		substring(0, 1).uppercase() + substring(1).lowercase()
	else
		this


private fun String.words(): List<String> {
	val words = mutableListOf<String>()

	var isInWord = false
	var currentWordEnd = -1
	var currentWordStart = -1
	var currentWordEndsWithDigit = false
	var currentWordEndsWithLowerCase = false
	var currentWordEndsWithUpperCase = false

	for ((index, character) in withIndex()) {
		val isLetter = character.isLetter()
		val isDigit = character.isDigit()

		if (isLetter || isDigit) {
			val isLowerCase = character.isLowerCase()
			val isUpperCase = character.isUpperCase()

			if (isInWord) {
				when {
					!currentWordEndsWithDigit && isDigit -> {
						words += substring(startIndex = currentWordStart, endIndex = index)

						currentWordStart = index
					}

					currentWordEndsWithLowerCase && isUpperCase -> {
						words += substring(startIndex = currentWordStart, endIndex = index)

						currentWordStart = index
					}

					currentWordEndsWithUpperCase && isLowerCase -> {
						if (currentWordStart < index - 1) {
							words += substring(startIndex = currentWordStart, endIndex = index - 1)
						}

						currentWordStart = index - 1
					}
				}
			}
			else {
				isInWord = true

				if (!isDigit || !currentWordEndsWithDigit) {
					if (currentWordStart >= 0) {
						words += substring(startIndex = currentWordStart, endIndex = currentWordEnd)
					}

					currentWordStart = index
				}
			}

			currentWordEndsWithDigit = isDigit
			currentWordEndsWithLowerCase = isLowerCase
			currentWordEndsWithUpperCase = isUpperCase
		}
		else if (isInWord) {
			currentWordEnd = index
			isInWord = false
		}
	}

	if (isInWord) {
		currentWordEnd = length
	}
	if (currentWordStart >= 0) {
		words += substring(startIndex = currentWordStart, endIndex = currentWordEnd)
	}

	return words
}
