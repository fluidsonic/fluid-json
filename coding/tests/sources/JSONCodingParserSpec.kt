package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.StringReader


internal object JSONCodingParserSpec : Spek({

	describe("JSONCodingParser") {

		it(".builder()") {
			JSONCodingParser.builder()
				.decodingWith(BooleanJSONCodec)
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}

			JSONCodingParser.builder()
				.decodingWith(listOf(BooleanJSONCodec))
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}

			val testContext = TestCoderContext()

			JSONCodingParser.builder(testContext)
				.decodingWith()
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}
		}

		it(".builder() doesn't add standard codecs if different base is provided") {
			try {
				JSONCodingParser.builder()
					.decodingWith(base = null)
					.build()
					.parseValueOfType<List<*>>("[]")

				throw AssertionError("JSONCodingParser without any codec provided unexpectedly uses codecs")
			}
			catch (e: JSONException) {
				// good
			}
		}

		it(".default") {
			anyData.testDecoding(JSONCodingParser.default::parseValue)
		}

		it(".parse()") {
			testParseMethod<Any>(true) { parseValue("") }
			testParseMethod<Any>(true) { parseValueOrNull("") }
			testParseMethod<Any>(null) { parseValueOrNull("") }

			testParseMethod<Any>(true) { parseValue(StringReader("")) }
			testParseMethod<Any>(true) { parseValueOrNull(StringReader("")) }
			testParseMethod<Any>(null) { parseValueOrNull(StringReader("")) }

			testParseMethod(listOf(true)) { parseValueOfType("") }
			testParseMethod(listOf(true)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as List<Boolean>?) { parseValueOfTypeOrNull("") }
			testParseMethod(listOf(true)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as List<Boolean>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(listOf(true)) { parseValueOfType(StringReader("")) }
			testParseMethod(listOf(true)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as List<Boolean>?) { parseValueOfTypeOrNull(StringReader("")) }

			testParseMethod(listOf(true, null)) { parseValueOfType("") }
			testParseMethod(listOf(true, null)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as List<Boolean?>?) { parseValueOfTypeOrNull("") }
			testParseMethod(listOf(true, null)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as List<Boolean?>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(listOf(true, null)) { parseValueOfType(StringReader("")) }
			testParseMethod(listOf(true, null)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as List<Boolean?>?) { parseValueOfTypeOrNull(StringReader("")) }

			testParseMethod(mapOf("key" to true)) { parseValueOfType("") }
			testParseMethod(mapOf("key" to true)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as Map<String, Boolean>?) { parseValueOfTypeOrNull("") }
			testParseMethod(mapOf("key" to true)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as Map<String, Boolean>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(mapOf("key" to true)) { parseValueOfType(StringReader("")) }
			testParseMethod(mapOf("key" to true)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as Map<String, Boolean>?) { parseValueOfTypeOrNull(StringReader("")) }

			testParseMethod(mapOf("key" to true, "key" to null)) { parseValueOfType("") }
			testParseMethod(mapOf("key" to true, "key" to null)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as Map<String, Boolean?>?) { parseValueOfTypeOrNull("") }
			testParseMethod(mapOf("key" to true, "key" to null)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as Map<String, Boolean?>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(mapOf("key" to true, "key" to null)) { parseValueOfType(StringReader("")) }
			testParseMethod(mapOf("key" to true, "key" to null)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as Map<String, Boolean?>?) { parseValueOfTypeOrNull(StringReader("")) }

			testParseMethod(mapOf("key" to true, null to true)) { parseValueOfType("") }
			testParseMethod(mapOf("key" to true, null to true)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as Map<String?, Boolean>?) { parseValueOfTypeOrNull("") }
			testParseMethod(mapOf("key" to true, null to true)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as Map<String?, Boolean>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(mapOf("key" to true, null to true)) { parseValueOfType(StringReader("")) }
			testParseMethod(mapOf("key" to true, null to true)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as Map<String?, Boolean>?) { parseValueOfTypeOrNull(StringReader("")) }

			testParseMethod(mapOf("key" to true, null to null)) { parseValueOfType("") }
			testParseMethod(mapOf("key" to true, null to null)) { parseValueOfTypeOrNull("") }
			testParseMethod(null as Map<String?, Boolean?>?) { parseValueOfTypeOrNull("") }
			testParseMethod(mapOf("key" to true, null to null)) { parseValueOfTypeOrNull("", it) }
			testParseMethod(null as Map<String?, Boolean?>?) { parseValueOfTypeOrNull("", it) }

			testParseMethod(mapOf("key" to true, null to null)) { parseValueOfType(StringReader("")) }
			testParseMethod(mapOf("key" to true, null to null)) { parseValueOfTypeOrNull(StringReader("")) }
			testParseMethod(null as Map<String?, Boolean?>?) { parseValueOfTypeOrNull(StringReader("")) }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun <reified Value : Any> testParseMethod(
	expectedValue: Value?,
	testBody: JSONCodingParser.(type: JSONCodingType<Value>) -> Value?
) {
	val expectedType = jsonCodingType<Value>()

	val parser = object : JSONCodingParser {

		override fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodingType<Value>, withTermination: Boolean): Value? {
			(valueType as JSONCodingType<*>).should.equal(expectedType)

			@Suppress("UNCHECKED_CAST")
			return expectedValue as Value?
		}
	}

	parser.testBody(expectedType).should.equal(expectedValue)
}
