package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JSONEncoderTest {

	@Test
	fun testBuilder() {
		StringWriter().let { writer ->
			JSONEncoder.builder()
				.codecs(BooleanJSONCodec)
				.destination(writer)
				.build()
				.apply {
					assert(context).toBe(JSONCodingContext.empty)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JSONEncoder.builder()
				.codecs(listOf(BooleanJSONCodec))
				.destination(writer)
				.build()
				.apply {
					assert(context).toBe(JSONCodingContext.empty)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		val testContext = TestCoderContext()

		StringWriter().let { writer ->
			JSONEncoder.builder(testContext)
				.codecs()
				.destination(JSONWriter.build(writer))
				.build()
				.apply {
					assert(context).toBe(testContext)
					writeBoolean(true)
					assert(writer.toString()).toBe("true")
				}
		}

		StringWriter().let { writer ->
			JSONEncoder.builder(testContext)
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
