package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JsonCodingParserTest {

	@Test
	fun testBuilder() {
		JsonCodingParser.builder()
			.decodingWith(BooleanJsonCodec)
			.build()
			.apply {
				// TODO check correct context
				assert(parseValue(StringReader("true"))).toBe(true)
			}

		JsonCodingParser.builder()
			.decodingWith(listOf(BooleanJsonCodec))
			.build()
			.apply {
				// TODO check correct context
				assert(parseValue(StringReader("true"))).toBe(true)
			}

		val testContext = TestCoderContext()

		JsonCodingParser.builder(testContext)
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
			JsonCodingParser.builder()
				.decodingWith(base = null)
				.build()
				.parseValueOfType<List<*>>("[]")

			throw AssertionError("JsonCodingParser without any codec provided unexpectedly uses codecs")
		}
		catch (e: JsonException) {
			// good
		}
	}


	@Test
	fun testDefault() {
		anyData.testDecoding(JsonCodingParser.default::parseValue)
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
		testBody: JsonCodingParser<*>.(type: JsonCodingType<Value>) -> Value?
	) {
		val expectedType = jsonCodingType<Value>()

		val parser = object : JsonCodingParser<JsonCodingContext> {

			override fun createDecoder(source: JsonReader) =
				error("not called")


			override fun <Value : Any> parseValueOfType(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean): Value {
				assert(valueType as JsonCodingType<*>).toBe(expectedType)

				@Suppress("UNCHECKED_CAST")
				return expectedValue as Value
			}


			override fun <Value : Any> parseValueOfTypeOrNull(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean): Value? {
				assert(valueType as JsonCodingType<*>).toBe(expectedType)

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
