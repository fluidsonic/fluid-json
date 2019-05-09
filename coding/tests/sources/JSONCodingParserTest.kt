package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test
import java.io.StringReader


internal object JSONCodingParserTest {

	@Test
	fun testBuilder() {
		JSONCodingParser.builder()
			.decodingWith(BooleanJSONCodec)
			.build()
			.apply {
				// TODO check correct context
				assert(parseValue(StringReader("true"))).toBe(true)
			}

		JSONCodingParser.builder()
			.decodingWith(listOf(BooleanJSONCodec))
			.build()
			.apply {
				// TODO check correct context
				assert(parseValue(StringReader("true"))).toBe(true)
			}

		val testContext = TestCoderContext()

		JSONCodingParser.builder(testContext)
			.decodingWith()
			.build()
			.apply {
				// TODO check correct context
				assert(parseValue(StringReader("true"))).toBe(true)
			}
	}


	@Test
	fun testBuilderWithDifferentBaseCodecs() {
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


	@Test
	fun testDefault() {
		anyData.testDecoding(JSONCodingParser.default::parseValue)
	}


	@Test
	fun testParse() {
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


	private inline fun <reified Value : Any> testParseMethod(
		expectedValue: Value?,
		testBody: JSONCodingParser<*>.(type: JSONCodingType<Value>) -> Value?
	) {
		val expectedType = jsonCodingType<Value>()

		val parser = object : JSONCodingParser<JSONCodingContext> {

			override fun createDecoder(source: JSONReader) =
				error("not called")


			override fun <Value : Any> parseValueOfType(source: JSONReader, valueType: JSONCodingType<Value>, withTermination: Boolean): Value {
				assert(valueType as JSONCodingType<*>).toBe(expectedType)

				@Suppress("UNCHECKED_CAST")
				return expectedValue as Value
			}


			override fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodingType<Value>, withTermination: Boolean): Value? {
				assert(valueType as JSONCodingType<*>).toBe(expectedType)

				@Suppress("UNCHECKED_CAST")
				return expectedValue as Value?
			}
		}

		if (expectedValue != null)
			assert(parser.testBody(expectedType)).toBe(expectedValue)
		else
			assert(parser.testBody(expectedType)).toBe(expectedValue)
	}
}
