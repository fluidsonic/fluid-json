package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.StringWriter


internal object JSONEncoderSpec : Spek({

	describe("JSONEncoder") {

		it(".builder()") {
			StringWriter().let { writer ->
				JSONEncoder.builder()
					.codecs(JSONCodecProvider.default)
					.destination(JSONWriter.build(writer))
					.build()
					.apply {
						context.should.equal(JSONCoderContext.empty)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			StringWriter().let { writer ->
				JSONEncoder.builder()
					.codecs(JSONCodecProvider.default)
					.destination(writer)
					.build()
					.apply {
						context.should.equal(JSONCoderContext.empty)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			StringWriter().let { writer ->
				JSONEncoder.builder()
					.codecs(BooleanJSONCodec)
					.destination(writer)
					.build()
					.apply {
						context.should.equal(JSONCoderContext.empty)
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
						context.should.equal(JSONCoderContext.empty)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}

			val testContext = TestCoderContext()

			StringWriter().let { writer ->
				JSONEncoder.builder(testContext)
					.codecs(JSONCodecProvider.default)
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
					.codecs(JSONCodecProvider.default)
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
