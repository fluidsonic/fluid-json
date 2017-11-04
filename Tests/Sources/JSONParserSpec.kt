package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.Reader
import java.io.StringReader


internal object JSONParserSpec : Spek({

	describe("JSONParser") {

		it(".builder()") {
			JSONParser.builder()
				.decodingWith(BooleanJSONCodec)
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}

			JSONParser.builder()
				.decodingWith(listOf(BooleanJSONCodec))
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}

			val testContext = TestCoderContext()

			JSONParser.builder(testContext)
				.decodingWith()
				.build()
				.apply {
					// TODO check correct context
					parseValue(StringReader("true")).should.equal(true)
				}
		}

		it(".default") {
			anyData.testDecoding(JSONParser.default::parseValue)
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
	testBody: JSONParser.(type: JSONCodableType<Value>) -> Value?
) {
	val expectedType = jsonCodableType<Value>()

	val parser = object : JSONParser {

		override fun <Value : Any> parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>): Value? {
			(valueType as JSONCodableType<*>).should.equal(expectedType)

			@Suppress("UNCHECKED_CAST")
			return expectedValue as Value?
		}
	}

	parser.testBody(expectedType).should.equal(expectedValue)
}
