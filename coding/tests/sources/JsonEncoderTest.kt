package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JsonEncoderTest {

	@Test
	fun testBuilder() {
		StringWriter().let { writer ->
			JsonEncoder.builder()
				.codecs(BooleanJsonCodec)
				.destination(writer)
				.build()
				.apply {
					assert(context).toBe(JsonCodingContext.empty)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JsonEncoder.builder()
				.codecs(listOf(BooleanJsonCodec))
				.destination(writer)
				.build()
				.apply {
					assert(context).toBe(JsonCodingContext.empty)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		val testContext = TestCoderContext()

		StringWriter().let { writer ->
			JsonEncoder.builder(testContext)
				.codecs()
				.destination(JsonWriter.build(writer))
				.build()
				.apply {
					assert(context).toBe(testContext)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JsonEncoder.builder(testContext)
				.codecs()
				.destination(writer)
				.build()
				.apply {
					assert(context).toBe(testContext)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}
	}
}
