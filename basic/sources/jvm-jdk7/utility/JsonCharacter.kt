package io.fluidsonic.json


internal object JsonCharacter {

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
			Digit.zero,
			Digit.one,
			Digit.two,
			Digit.three,
			Digit.four,
			Digit.five,
			Digit.six,
			Digit.seven,
			Digit.eight,
			Digit.nine,
			Letter.a,
			Letter.b,
			Letter.c,
			Letter.d,
			Letter.e,
			Letter.f,
			Letter.A,
			Letter.B,
			Letter.C,
			Letter.D,
			Letter.E,
			Letter.F -> true
			else -> false
		}


	fun isValueBoundary(character: Int) =
		when (character) {
			end,
			Symbol.colon,
			Symbol.comma,
			Symbol.leftCurlyBracket,
			Symbol.leftSquareBracket,
			Symbol.rightCurlyBracket,
			Symbol.rightSquareBracket,
			Whitespace.carriageReturn,
			Whitespace.characterTabulation,
			Whitespace.lineFeed,
			Whitespace.space -> true
			else -> false
		}


	fun isWhitespace(character: Int) =
		when (character) {
			Whitespace.carriageReturn,
			Whitespace.characterTabulation,
			Whitespace.lineFeed,
			Whitespace.space -> true
			else -> false
		}


	fun parseHexDigit(character: Int) =
		when (character) {
			Digit.zero,
			Digit.one,
			Digit.two,
			Digit.three,
			Digit.four,
			Digit.five,
			Digit.six,
			Digit.seven,
			Digit.eight,
			Digit.nine ->
				character - Digit.zero

			Letter.a,
			Letter.b,
			Letter.c,
			Letter.d,
			Letter.e,
			Letter.f ->
				character - Letter.a + 10

			Letter.A,
			Letter.B,
			Letter.C,
			Letter.D,
			Letter.E,
			Letter.F ->
				character - Letter.A + 10

			else ->
				throw IllegalArgumentException("${toString(character)} is not a hex digit.")
		}


	fun toString(character: Int) =
		when {
			character == end -> "end of input"
			isControl(character) -> "control character $character"
			else -> "character '${character.toChar()}'"
		}


	fun toString(vararg characters: Int) =
		characters.joinToString(" or ") { toString(it) }
}
