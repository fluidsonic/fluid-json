package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


class JSONParserRejectSpec : Spek({

	describe("JSONParser rejects") {

		it("unknown constants") {
			failToParse("void")
		}

		it("partial constants") {
			failToParse("tru")
			failToParse("fals")
			failToParse("nul")
		}

		it("extra data after constants") {
			failToParse("true2")
			failToParse("false2")
			failToParse("null2")
			failToParse("null\"")
		}

		it("multiple values") {
			failToParse("1 2")
			failToParse("1, 2")
			failToParse("true false")
			failToParse("null null")
		}

		it("non-decimal number formats") {
			failToParse("0b0")
			failToParse("0o0")
			failToParse("0x0")
		}

		it("non-zero integrals with leading zero") {
			failToParse("01")
			failToParse("01.0")
		}

		it("positive number sign") {
			failToParse("+0")
			failToParse("+1")
			failToParse("+-1")
		}

		it("incomplete or weird numbers") {
			failToParse("-true")
			failToParse("-.")
			failToParse("-e")
			failToParse("0.")
			failToParse("0e")
			failToParse("1.")
			failToParse("1.e")
			failToParse("1e")
			failToParse("1ee")
			failToParse("1e.")
			failToParse("1e+")
			failToParse("1e+e")
			failToParse("1e-")
			failToParse("1e-e")
		}

		it("extra data after number") {
			failToParse("0.0z")
			failToParse("0.0\"")
		}

		it("unterminated escape sequence") {
			failToParse("\"test")
			failToParse("\"test\\uDD\"")
		}

		it("unknown escape sequences") {
			failToParse("\"\\a\"")
			failToParse("\"\\0\"")
			failToParse("\"\\,\"")
			failToParse("\"\\:\"")
			failToParse("\"\\[\"")
		}

		it("invalid unicode escape sequences") {
			failToParse("\"\\u000Z\"")
			failToParse("\"\\u00ZZ\"")
			failToParse("\"\\u0ZZZ\"")
			failToParse("\"\\uZZZZ\"")
		}

		it("unescaped control characters") {
			listOf(
				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
				'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
				'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
			)
				.forEach { failToParse("\"$it\"") }
		}

		it("unterminated array") {
			failToParse("[")
			failToParse("[1")
			failToParse("[1,")
		}

		it("extra data after array") {
			failToParse("[1]b")
		}

		it("extra data after array element") {
			failToParse("[1 1]")
		}

		it("misplaced commas in arrays") {
			failToParse("[,1,1]")
			failToParse("[1,,1]")
			failToParse("[1,1,]")
		}

		it("unterminated object") {
			failToParse("{")
			failToParse("{\"")
			failToParse("{\"x")
			failToParse("{\"x\"")
			failToParse("{\"x\":")
			failToParse("{\"x\":1")
			failToParse("{\"x\":1,")
		}

		it("extra data after object") {
			failToParse("{\"key\": 1}b")
		}

		it("extra data before object key") {
			failToParse("{true \"key\": 1}")
		}

		it("extra data after object key") {
			failToParse("{\"key\" true: 1}")
		}

		it("extra data before object value") {
			failToParse("{\"key\"::1}")
		}

		it("extra data after object value") {
			failToParse("{\"key\":1:}")
			failToParse("{\"key\":1 \"key\"}")
		}

		it("misplaced commas in objects") {
			failToParse("{,\"key0\":1,\"key1\":1}")
			failToParse("{\"key0\":1,,\"key1\":1}")
			failToParse("{\"key0\":1,\"key1\":1,}")
		}

		it("a anything not being a list of expected") {
			failToParseList("null")
			failToParseList("?")
			failToParseList("1")
			failToParseList("{}")
		}

		it("a map as requested") {
			failToParseMap("null")
			failToParseMap("?")
			failToParseMap("1")
			failToParseMap("[]")
		}
	}
}) {

	private companion object {

		inline fun shouldFailWithJSONException(body: () -> Unit) {
			try {
				body()
				throw AssertionError("should fail with a JSONException")
			}
			catch (e: JSONException) {
				// good
			}
		}


		fun failToParse(string: String) {
			shouldFailWithJSONException { JSONParser().parse(string) }
		}


		fun failToParseList(string: String) {
			shouldFailWithJSONException { JSONParser().parseList(string) }
		}


		fun failToParseMap(string: String) {
			shouldFailWithJSONException { JSONParser().parseMap(string) }
		}
	}
}
