package com.github.fluidsonic.fluid.json

import com.github.fluidsonic.fluid.json.EnumJSONTransformation.*


sealed class EnumJSONTransformation {

	class Name(val case: Case? = null) : EnumJSONTransformation()
	object Ordinal : EnumJSONTransformation()
	class ToString(val case: Case? = null) : EnumJSONTransformation()


	@Suppress("EnumEntryName")
	enum class Case {

		lowerCamelCase,
		`lower-kebab-case`,
		lower_snake_case,
		lowercase,
		`lowercase words`,
		UpperCamelCase,
		`UPPER-KEBAB-CASE`,
		UPPER_SNAKE_CASE,
		UPPERCASE,
		`UPPERCASE WORDS`
	}
}


private fun wordToLowerCamelCase(index: Int, word: String) =
	if (index == 0)
		word.toLowerCase()
	else
		word.camelize()


internal fun Case?.convert(string: String) =
	when (this) {
		null -> string
		Case.lowerCamelCase -> string.words().mapIndexed(::wordToLowerCamelCase).joinToString(separator = "")
		Case.`lower-kebab-case` -> string.words().joinToString(separator = "-").toLowerCase()
		Case.lower_snake_case -> string.words().joinToString(separator = "_").toLowerCase()
		Case.lowercase -> string.toLowerCase()
		Case.`lowercase words` -> string.words().joinToString(separator = " ").toLowerCase()
		Case.UpperCamelCase -> string.words().joinToString(separator = "") { it.camelize() }
		Case.`UPPER-KEBAB-CASE` -> string.words().joinToString(separator = "-").toUpperCase()
		Case.UPPER_SNAKE_CASE -> string.words().joinToString(separator = "_").toUpperCase()
		Case.UPPERCASE -> string.toUpperCase()
		Case.`UPPERCASE WORDS` -> string.words().joinToString(separator = " ").toUpperCase()
	}


private fun String.camelize() =
	if (isNotEmpty())
		substring(0, 1).toUpperCase() + substring(1).toLowerCase()
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
