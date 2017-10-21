package tests

import com.github.fluidsonic.fluid.json.Character
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object CharacterUtilitySpec : Spek({

	describe("Character Utility") {

		it(".isControl()") {
			val boundaries = (0 .. 0x1F).toSet()
			for (character in 0 .. 0xFF)
				Character.isControl(character).should.equal(boundaries.contains(character))
		}

		it(".isDigit()") {
			val boundaries = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
			for (character in 0 .. 0xFF) {
				Character.isDigit(character).should.equal(boundaries.contains(character.toChar()))
			}

			Character.isDigit(Character.end).should.be.`false`
		}

		it(".isHexDigit()") {
			val boundaries = setOf(
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f',
				'A', 'B', 'C', 'D', 'E', 'F'
			)
			for (character in 0 .. 0xFF) {
				Character.isHexDigit(character).should.equal(boundaries.contains(character.toChar()))
			}

			Character.isHexDigit(Character.end).should.be.`false`
		}

		it(".isValueBoundary()") {
			val boundaries = setOf(',', ':', ',', '{', '[', '}', ']', '\r', '\t', '\n', ' ')
			for (character in 0 .. 0xFF) {
				Character.isValueBoundary(character).should.equal(boundaries.contains(character.toChar()))
			}

			Character.isValueBoundary(Character.end).should.be.`true`
		}

		it(".isWhitespace()") {
			val boundaries = setOf(' ', '\n', '\r', '\t')
			for (character in 0 .. 0xFF) {
				Character.isWhitespace(character).should.equal(boundaries.contains(character.toChar()))
			}

			Character.isWhitespace(Character.end).should.be.`false`
		}

		it(".parseHexDigit()") {
			val expectValues = mapOf(
				'0' to 0, '1' to 1, '2' to 2, '3' to 3, '4' to 4, '5' to 5, '6' to 6, '7' to 7, '8' to 8, '9' to 9,
				'a' to 10, 'b' to 11, 'c' to 12, 'd' to 13, 'e' to 14, 'f' to 15,
				'A' to 10, 'B' to 11, 'C' to 12, 'D' to 13, 'E' to 14, 'F' to 15
			)
			for (character in 0 .. 0xFF) {
				val expectValue = expectValues[character.toChar()]
				if (expectValue != null) {
					Character.parseHexDigit(character).should.equals(expectValue)
				}
				else {
					try {
						Character.parseHexDigit(character)
						throw AssertionError("Character.parseHexDigit() should fail for '$character'")
					}
					catch (e: Exception) {
						// good
					}
				}
			}

			try {
				Character.parseHexDigit(Character.end)
				throw AssertionError("Character.parseHexDigit() should fail for Character.end")
			}
			catch (e: Exception) {
				// good
			}
		}
	}
})
