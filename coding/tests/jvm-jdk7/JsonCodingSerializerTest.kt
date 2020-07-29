package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


class JsonCodingSerializerTest {

	@Test
	fun testBuilder() {
		JsonCodingSerializer.builder()
			.encodingWith(BooleanJsonCodec)
			.build()
			.apply {
				// TODO check correct context
				expect(serializeValue(true)).toBe("true")
			}

		JsonCodingSerializer.builder()
			.encodingWith(listOf(BooleanJsonCodec))
			.build()
			.apply {
				// TODO check correct context
				expect(serializeValue(true)).toBe("true")
			}

		val testContext = TestCoderContext()

		JsonCodingSerializer.builder(testContext)
			.encodingWith()
			.build()
			.apply {
				// TODO check correct context
				expect(serializeValue(true)).toBe("true")
			}
	}


	@Test
	fun testBuilderWithDifferentBaseCodecs() {
		try {
			JsonCodingSerializer.builder()
				.encodingWith(base = null)
				.build()
				.serializeValue(emptyList<Any>())

			throw AssertionError("JsonCodingSerializer without any codec provided unexpectedly uses codecs")
		}
		catch (e: JsonException) {
			// good
		}
	}


	@Test
	fun testDefault() {
		anyData.testEncoding(JsonCodingSerializer.default::serializeValue)
	}
}
