package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.StandardParser
import com.github.fluidsonic.fluid.json.parseValue
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object StandardParserRejectSpec : SubjectSpek<JSONParser<JSONCoderContext>>({

	subject {
		StandardParser(JSONCoderContext.empty) { source, context ->
			JSONDecoder.builder(context)
				.codecs(JSONCodecResolver.default)
				.source(source)
				.build()
		}
	}


	describe("StandardParser rejects") {

		it("unknown constants") {
			subject.failToParse("void")
		}

		it("partial constants") {
			subject.failToParse("tru")
			subject.failToParse("fals")
			subject.failToParse("nul")
		}

		it("extra data after constants") {
			subject.failToParse("true2")
			subject.failToParse("false2")
			subject.failToParse("null2")
			subject.failToParse("null\"")
		}

		it("multiple values") {
			subject.failToParse("1 2")
			subject.failToParse("1, 2")
			subject.failToParse("true false")
			subject.failToParse("null null")
		}

		it("non-decimal number formats") {
			subject.failToParse("0b0")
			subject.failToParse("0o0")
			subject.failToParse("0x0")
		}

		it("non-zero integrals with leading zero") {
			subject.failToParse("01")
			subject.failToParse("01.0")
		}

		it("positive number sign") {
			subject.failToParse("+0")
			subject.failToParse("+1")
			subject.failToParse("+-1")
		}

		it("incomplete or weird numbers") {
			subject.failToParse("-true")
			subject.failToParse("-.")
			subject.failToParse("-e")
			subject.failToParse("0.")
			subject.failToParse("0e")
			subject.failToParse("1.")
			subject.failToParse("1.e")
			subject.failToParse("1e")
			subject.failToParse("1ee")
			subject.failToParse("1e.")
			subject.failToParse("1e+")
			subject.failToParse("1e+e")
			subject.failToParse("1e-")
			subject.failToParse("1e-e")
		}

		it("extra data after number") {
			subject.failToParse("0.0z")
			subject.failToParse("0.0\"")
		}

		it("unterminated string") {
			subject.failToParse("\"test")
			subject.failToParse("\"test\\uDD\"")
			subject.failToParse("{\"test")
			subject.failToParse("{\"key\":\"test")
		}

		it("unknown escape sequences") {
			subject.failToParse("\"\\a\"")
			subject.failToParse("\"\\0\"")
			subject.failToParse("\"\\,\"")
			subject.failToParse("\"\\:\"")
			subject.failToParse("\"\\[\"")
		}

		it("invalid unicode escape sequences") {
			subject.failToParse("\"\\u000Z\"")
			subject.failToParse("\"\\u00ZZ\"")
			subject.failToParse("\"\\u0ZZZ\"")
			subject.failToParse("\"\\uZZZZ\"")
		}

		it("unescaped control characters") {
			listOf(
				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
				'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
				'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
			)
				.forEach { subject.failToParse("\"$it\"") }
		}

		it("unterminated array") {
			subject.failToParse("[")
			subject.failToParse("[1")
			subject.failToParse("[1,")
		}

		it("extra data after array") {
			subject.failToParse("[1]b")
		}

		it("extra data after array element") {
			subject.failToParse("[1 1]")
		}

		it("misplaced commas in arrays") {
			subject.failToParse("[,1,1]")
			subject.failToParse("[1,,1]")
			subject.failToParse("[1,1,]")
		}

		it("unterminated object") {
			subject.failToParse("{")
			subject.failToParse("{\"")
			subject.failToParse("{\"x")
			subject.failToParse("{\"x\"")
			subject.failToParse("{\"x\":")
			subject.failToParse("{\"x\":1")
			subject.failToParse("{\"x\":1,")
		}

		it("extra data after object") {
			subject.failToParse("{\"key\": 1}b")
		}

		it("extra data before object key") {
			subject.failToParse("{true \"key\": 1}")
		}

		it("extra data after object key") {
			subject.failToParse("{\"key\" true: 1}")
		}

		it("extra data before object value") {
			subject.failToParse("{\"key\"::1}")
		}

		it("extra data after object value") {
			subject.failToParse("{\"key\":1:}")
			subject.failToParse("{\"key\":1 \"key\"}")
		}

		it("misplaced commas in objects") {
			subject.failToParse("{,\"key0\":1,\"key1\":1}")
			subject.failToParse("{\"key0\":1,,\"key1\":1}")
			subject.failToParse("{\"key0\":1,\"key1\":1,}")
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun shouldFailWithJSONException(body: () -> Unit) {
	try {
		body()
		throw AssertionError("should fail with a JSONException")
	}
	catch (e: JSONException) {
		// good
	}
}


private fun JSONParser<JSONCoderContext>.failToParse(string: String) {
	shouldFailWithJSONException { parseValue(string) }
}
