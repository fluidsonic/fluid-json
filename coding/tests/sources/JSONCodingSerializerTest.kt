package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


internal object JSONCodingSerializerTest {

	@Test
	fun testBuilder() {
		JSONCodingSerializer.builder()
			.encodingWith(BooleanJSONCodec)
			.build()
			.apply {
				// TODO check correct context
				assert(serializeValue(true)).toBe("true")
			}

		JSONCodingSerializer.builder()
			.encodingWith(listOf(BooleanJSONCodec))
			.build()
			.apply {
				// TODO check correct context
				assert(serializeValue(true)).toBe("true")
			}

		val testContext = TestCoderContext()

		JSONCodingSerializer.builder(testContext)
			.encodingWith()
			.build()
			.apply {
				// TODO check correct context
				assert(serializeValue(true)).toBe("true")
			}
	}


	@Test
	fun testBuilderWithDifferentBaseCodecs() {
		try {
			JSONCodingSerializer.builder()
				.encodingWith(base = null)
				.build()
				.serializeValue(emptyList<Any>())

			throw AssertionError("JSONCodingSerializer without any codec provided unexpectedly uses codecs")
		}
		catch (e: JSONException) {
			// good
		}
	}


	@Test
	fun testDefault() {
		anyData.testEncoding(JSONCodingSerializer.default::serializeValue)
	}
}
