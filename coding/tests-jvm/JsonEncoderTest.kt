package tests.coding

import io.fluidsonic.json.*
import java.io.*
import kotlin.test.*


class JsonEncoderTest {

	@Test
	fun testBuilder() {
		StringWriter().let { writer ->
			JsonEncoder.builder()
				.codecs(BooleanJsonCodec)
				.destination(writer)
				.build()
				.apply {
					expect(context).toBe(JsonCodingContext.empty)
					writeBoolean(true)
					expect(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JsonEncoder.builder()
				.codecs(listOf(BooleanJsonCodec))
				.destination(writer)
				.build()
				.apply {
					expect(context).toBe(JsonCodingContext.empty)
					writeBoolean(true)
					expect(writer.toString()).toBe("true")
				}
		}

		val testContext = TestCoderContext()

		StringWriter().let { writer ->
			JsonEncoder.builder(testContext)
				.codecs()
				.destination(JsonWriter.build(writer))
				.build()
				.apply {
					expect(context).toBe(testContext)
					writeBoolean(true)
					expect(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JsonEncoder.builder(testContext)
				.codecs()
				.destination(writer)
				.build()
				.apply {
					expect(context).toBe(testContext)
					writeBoolean(true)
					expect(writer.toString()).toBe("true")
				}
		}
	}
}
