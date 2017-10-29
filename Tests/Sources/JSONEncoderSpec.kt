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
					.codecs(JSONCodecProvider.nonRecursive)
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
					.codecs(JSONCodecProvider.nonRecursive)
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
					.codecs(JSONCodecProvider.nonRecursive)
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
					.codecs(JSONCodecProvider.nonRecursive)
					.destination(writer)
					.build()
					.apply {
						context.should.equal(testContext)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}
		}

		it(".writeValueOrNull()") {
			var expectedValue: Any? = null

			val encoder = object : DummyJSONEncoder() {
				override fun writeValue(value: Any) {
					value.should.equal(expectedValue)
				}

				override fun writeNull() {
					(null as Any?).should.equal(expectedValue)
				}
			}

			expectedValue = "okay"
			encoder.writeValueOrNull("okay")

			expectedValue = null
			encoder.writeValueOrNull(null)
		}

		it(".writeMapElement()") {
			var expectedKey: Any? = null
			var expectedValue: Any? = null

			val encoder = object : DummyJSONEncoder() {
				override fun writeMapKey(value: String) {
					(value as Any?).should.equal(expectedKey)
				}

				override fun writeValue(value: Any) {
					value.should.equal(expectedValue)
				}

				override fun writeNull() {
					(null as Any?).should.equal(expectedValue)
				}
			}

			expectedKey = "key"
			expectedValue = "value"
			encoder.writeMapElement(key = expectedKey, value = expectedValue!!)
			encoder.writeMapElement(key = expectedKey, value = expectedValue!!, skipIfNull = false)
			encoder.writeMapElement(key = expectedKey, value = expectedValue, skipIfNull = true)

			expectedValue = null
			encoder.writeMapElement(key = expectedKey, value = expectedValue, skipIfNull = false)

			expectedKey = null
			encoder.writeMapElement(key = "none", value = expectedValue, skipIfNull = true)
		}

		it(".writeValue()") {
			val expectedValue = "okay"

			val encoder = object : DummyJSONEncoder() {
				override fun writeValue(value: Any) {
					value.should.equal(expectedValue)
				}
			}

			encoder.writeValue("okay")
		}
	}
})
