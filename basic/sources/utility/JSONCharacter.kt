package com.github.fluidsonic.fluid.json


internal object JSONCharacter {

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
		const val A = 'A'.toInt()
		const val b = 'b'.toInt()
		const val B = 'B'.toInt()
		const val c = 'c'.toInt()
		const val C = 'C'.toInt()
		const val d = 'd'.toInt()
		const val D = 'D'.toInt()
		const val e = 'e'.toInt()
		const val E = 'E'.toInt()
		const val f = 'f'.toInt()
		const val F = 'F'.toInt()
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
		const val solidus = '/'.toInt()
	}

	object Whitespace {

		const val carriageReturn = '\r'.toInt()
		const val characterTabulation = '\t'.toInt()
		const val lineFeed = '\n'.toInt()
		const val space = ' '.toInt()
	}

	const val end = -1


	fun isControl(character: Int) =
		character in 0 .. 0x1F


	fun isDigit(character: Int) =
		character in Digit.zero .. Digit.nine


	fun isHexDigit(character: Int) =
		when (character) {
			JSONCharacter.Digit.zero,
			JSONCharacter.Digit.one,
			JSONCharacter.Digit.two,
			JSONCharacter.Digit.three,
			JSONCharacter.Digit.four,
			JSONCharacter.Digit.five,
			JSONCharacter.Digit.six,
			JSONCharacter.Digit.seven,
			JSONCharacter.Digit.eight,
			JSONCharacter.Digit.nine,
			JSONCharacter.Letter.a,
			JSONCharacter.Letter.b,
			JSONCharacter.Letter.c,
			JSONCharacter.Letter.d,
			JSONCharacter.Letter.e,
			JSONCharacter.Letter.f,
			JSONCharacter.Letter.A,
			JSONCharacter.Letter.B,
			JSONCharacter.Letter.C,
			JSONCharacter.Letter.D,
			JSONCharacter.Letter.E,
			JSONCharacter.Letter.F -> true
			else -> false
		}


	fun isValueBoundary(character: Int) =
		when (character) {
			JSONCharacter.end,
			JSONCharacter.Symbol.colon,
			JSONCharacter.Symbol.comma,
			JSONCharacter.Symbol.leftCurlyBracket,
			JSONCharacter.Symbol.leftSquareBracket,
			JSONCharacter.Symbol.rightCurlyBracket,
			JSONCharacter.Symbol.rightSquareBracket,
			JSONCharacter.Whitespace.carriageReturn,
			JSONCharacter.Whitespace.characterTabulation,
			JSONCharacter.Whitespace.lineFeed,
			JSONCharacter.Whitespace.space -> true
			else -> false
		}


	fun isWhitespace(character: Int) =
		when (character) {
			JSONCharacter.Whitespace.carriageReturn,
			JSONCharacter.Whitespace.characterTabulation,
			JSONCharacter.Whitespace.lineFeed,
			JSONCharacter.Whitespace.space -> true
			else -> false
		}


	fun parseHexDigit(character: Int) =
		when (character) {
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
				character - JSONCharacter.Digit.zero

			JSONCharacter.Letter.a,
			JSONCharacter.Letter.b,
			JSONCharacter.Letter.c,
			JSONCharacter.Letter.d,
			JSONCharacter.Letter.e,
			JSONCharacter.Letter.f ->
				character - JSONCharacter.Letter.a + 10

			JSONCharacter.Letter.A,
			JSONCharacter.Letter.B,
			JSONCharacter.Letter.C,
			JSONCharacter.Letter.D,
			JSONCharacter.Letter.E,
			JSONCharacter.Letter.F ->
				character - JSONCharacter.Letter.A + 10

			else ->
				throw IllegalArgumentException("${toString(character)} is not a hex digit.")
		}


	fun toString(character: Int) =
		when {
			character == end -> "end of input"
			isControl(character) -> "control character $character"
			else -> """'${Character.toString(character.toChar())}'"""
		}


	fun toString(vararg characters: Int) =
		characters.joinToString(" or ") { toString(it) }
}
