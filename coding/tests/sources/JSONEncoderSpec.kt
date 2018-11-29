package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.StringWriter


internal object JSONEncoderSpec : Spek({

	describe("JSONEncoder") {

		it(".builder()") {
			StringWriter().let { writer ->
				JSONEncoder.builder()
					.codecs(BooleanJSONCodec)
					.destination(writer)
					.build()
					.apply {
						context.should.equal(JSONCodingContext.empty)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			StringWriter().let { writer ->
				JSONEncoder.builder()
					.codecs(listOf(BooleanJSONCodec))
					.destination(writer)
					.build()
					.apply {
						context.should.equal(JSONCodingContext.empty)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			val testContext = TestCoderContext()

			StringWriter().let { writer ->
				JSONEncoder.builder(testContext)
					.codecs()
					.destination(JSONWriter.build(writer))
					.build()
					.apply {
						context.should.equal(testContext)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			StringWriter().let { writer ->
				JSONEncoder.builder(testContext)
					.codecs()
					.destination(writer)
					.build()
					.apply {
						context.should.equal(testContext)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}
		}
	}
})
