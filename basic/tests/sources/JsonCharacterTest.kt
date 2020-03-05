package tests.basic

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object JsonCharacterTest {

	@Test
	fun testIsControl() {
		val boundaries = (0 .. 0x1F).toSet()
		for (character in 0 .. 0xFF)
			expect(JsonCharacter.isControl(character)).toBe(boundaries.contains(character))
	}


	@Test
	fun testIsDigit() {
		val boundaries = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
		for (character in 0 .. 0xFF) {
			expect(JsonCharacter.isDigit(character)).toBe(boundaries.contains(character.toChar()))
		}

		expect(JsonCharacter.isDigit(JsonCharacter.end)).toBe(false)
	}


	@Test
	fun testIsHexDigit() {
		val boundaries = setOf(
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f',
			'A', 'B', 'C', 'D', 'E', 'F'
		)
		for (character in 0 .. 0xFF) {
			expect(JsonCharacter.isHexDigit(character)).toBe(boundaries.contains(character.toChar()))
		}

		expect(JsonCharacter.isHexDigit(JsonCharacter.end)).toBe(false)
	}


	@Test
	fun testIsValueBoundary() {
		val boundaries = setOf(',', ':', ',', '{', '[', '}', ']', '\r', '\t', '\n', ' ')
		for (character in 0 .. 0xFF) {
			expect(JsonCharacter.isValueBoundary(character)).toBe(boundaries.contains(character.toChar()))
		}

		expect(JsonCharacter.isValueBoundary(JsonCharacter.end)).toBe(true)
	}


	@Test
	fun testIsWhitespace() {
		val boundaries = setOf(' ', '\n', '\r', '\t')
		for (character in 0 .. 0xFF) {
			expect(JsonCharacter.isWhitespace(character)).toBe(boundaries.contains(character.toChar()))
		}

		expect(JsonCharacter.isWhitespace(JsonCharacter.end)).toBe(false)
	}


	@Test
	fun testParseHexDigit() {
		val expectValues = mapOf(
			'0' to 0, '1' to 1, '2' to 2, '3' to 3, '4' to 4, '5' to 5, '6' to 6, '7' to 7, '8' to 8, '9' to 9,
			'a' to 10, 'b' to 11, 'c' to 12, 'd' to 13, 'e' to 14, 'f' to 15,
			'A' to 10, 'B' to 11, 'C' to 12, 'D' to 13, 'E' to 14, 'F' to 15
		)
		for (character in 0 .. 0xFF) {
			val expectValue = expectValues[character.toChar()]
			if (expectValue != null) {
				expect(JsonCharacter.parseHexDigit(character)).toBe(expectValue)
			}
			else {
				try {
					JsonCharacter.parseHexDigit(character)
					throw AssertionError("Character.parseHexDigit() should fail for '$character'")
				}
				catch (e: Exception) {
					// good
				}
			}
		}

		try {
			JsonCharacter.parseHexDigit(JsonCharacter.end)
			throw AssertionError("Character.parseHexDigit() should fail for Character.end")
		}
		catch (e: Exception) {
			// good
		}
	}
}
