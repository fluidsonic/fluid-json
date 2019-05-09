package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JSONDecoderTest {

	@Test
	fun testBuilder() {
		JSONDecoder.builder()
			.codecs(BooleanJSONCodec)
			.source("true")
			.build()
			.apply {
				assert(context).toBe(JSONCodingContext.empty)
				assert(readBoolean()).toBe(true)
			}

		JSONDecoder.builder()
			.codecs(listOf(BooleanJSONCodec))
			.source(StringReader("true"))
			.build()
			.apply {
				assert(context).toBe(JSONCodingContext.empty)
				assert(readBoolean()).toBe(true)
			}

		val testContext = TestCoderContext()

		JSONDecoder.builder(testContext)
			.codecs()
			.source(JSONReader.build(StringReader("true")))
			.build()
			.apply {
				assert(context).toBe(testContext)
				assert(readBoolean()).toBe(true)
			}
	}


	@Test
	fun testReadShortcuts() {
		testReadMethod<Any>("1") { readValueOrNull() }
		testReadMethod<Any>(null) { readValueOrNull() }

		testReadMethod("1") { readValueOfType() }
		testReadMethod("1") { readValueOfTypeOrNull() }
		testReadMethod("1") { readValueOfTypeOrNull(it) }
		testReadMethod<String>(null) { readValueOfTypeOrNull() }
		testReadMethod<String>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(listOf("1")) { readValueOfType() }
		testReadMethod(listOf("1")) { readValueOfTypeOrNull() }
		testReadMethod(listOf("1")) { readValueOfTypeOrNull(it) }
		testReadMethod<List<String>>(null) { readValueOfTypeOrNull() }
		testReadMethod<List<String>>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(listOf("1", null)) { readValueOfType() }
		testReadMethod(listOf("1", null)) { readValueOfTypeOrNull() }
		testReadMethod(listOf("1", null)) { readValueOfTypeOrNull(it) }
		testReadMethod<List<String?>>(null) { readValueOfTypeOrNull() }
		testReadMethod<List<String?>>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(mapOf("1" to true)) { readValueOfType() }
		testReadMethod(mapOf("1" to true)) { readValueOfTypeOrNull() }
		testReadMethod(mapOf("1" to true)) { readValueOfTypeOrNull(it) }
		testReadMethod<Map<String, Boolean>>(null) { readValueOfTypeOrNull() }
		testReadMethod<Map<String, Boolean>>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(mapOf("1" to true, "2" to null)) { readValueOfType() }
		testReadMethod(mapOf("1" to true, "2" to null)) { readValueOfTypeOrNull() }
		testReadMethod(mapOf("1" to true, "2" to null)) { readValueOfTypeOrNull(it) }
		testReadMethod<Map<String, Boolean?>>(null) { readValueOfTypeOrNull() }
		testReadMethod<Map<String, Boolean?>>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(mapOf("1" to true, null to true)) { readValueOfType() }
		testReadMethod(mapOf("1" to true, null to true)) { readValueOfTypeOrNull() }
		testReadMethod(mapOf("1" to true, null to true)) { readValueOfTypeOrNull(it) }
		testReadMethod<Map<String, Boolean?>>(null) { readValueOfTypeOrNull() }
		testReadMethod<Map<String, Boolean?>>(null) { readValueOfTypeOrNull(it) }

		testReadMethod(mapOf("1" to true, null to null)) { readValueOfType() }
		testReadMethod(mapOf("1" to true, null to null)) { readValueOfTypeOrNull() }
		testReadMethod(mapOf("1" to true, null to null)) { readValueOfTypeOrNull(it) }
		testReadMethod<Map<String?, Boolean?>>(null) { readValueOfTypeOrNull() }
		testReadMethod<Map<String?, Boolean?>>(null) { readValueOfTypeOrNull(it) }
	}


	private inline fun <reified Value : Any> testReadMethod(
		expectedValue: Value?,
		testBody: JSONDecoder<JSONCodingContext>.(type: JSONCodingType<Value>) -> Value?
	) {
		val expectedType = jsonCodingType<Value>()

		val decoder = object : JSONDecoder<JSONCodingContext>, JSONReader by DummyJSONReader() {

			override val context: JSONCodingContext
				get() = error("")


			override val nextToken: JSONToken?
				get() = if (expectedValue == null) JSONToken.nullValue else JSONToken.stringValue


			override fun readNull(): Nothing? =
				null


			override fun readValue() =
				super<JSONDecoder>.readValue()


			override fun <Value : Any> readValueOfType(valueType: JSONCodingType<Value>): Value {
				assert((valueType as JSONCodingType<*>)).toBe(expectedType)

				@Suppress("UNCHECKED_CAST")
				return expectedValue as Value
			}
		}

		if (expectedValue != null)
			assert(decoder.testBody(expectedType)).toBe(expectedValue)
		else
			assert(decoder.testBody(expectedType)).toBe(expectedValue)
	}
}
