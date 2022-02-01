package io.fluidsonic.json


internal object JsonCharacter {

	object Digit {

		const val one = '1'.code
		const val two = '2'.code
		const val three = '3'.code
		const val four = '4'.code
		const val five = '5'.code
		const val six = '6'.code
		const val seven = '7'.code
		const val eight = '8'.code
		const val nine = '9'.code
		const val zero = '0'.code
	}

	object Letter {

		const val a = 'a'.code
		const val A = 'A'.code
		const val b = 'b'.code
		const val B = 'B'.code
		const val c = 'c'.code
		const val C = 'C'.code
		const val d = 'd'.code
		const val D = 'D'.code
		const val e = 'e'.code
		const val E = 'E'.code
		const val f = 'f'.code
		const val F = 'F'.code
		const val l = 'l'.code
		const val n = 'n'.code
		const val r = 'r'.code
		const val s = 's'.code
		const val t = 't'.code
		const val u = 'u'.code
	}

	object Symbol {

		const val colon = ':'.code
		const val comma = ','.code
		const val fullStop = '.'.code
		const val hyphenMinus = '-'.code
		const val leftCurlyBracket = '{'.code
		const val leftSquareBracket = '['.code
		const val plusSign = '+'.code
		const val quotationMark = '"'.code
		const val reverseSolidus = '\\'.code
		const val rightCurlyBracket = '}'.code
		const val rightSquareBracket = ']'.code
		const val solidus = '/'.code
	}

	object Whitespace {

		const val carriageReturn = '\r'.code
		const val characterTabulation = '\t'.code
		const val lineFeed = '\n'.code
		const val space = ' '.code
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
