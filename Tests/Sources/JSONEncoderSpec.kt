package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONWriter
import com.github.fluidsonic.fluid.json.writeEncodableOrNull
import com.github.fluidsonic.fluid.json.writeMapElement
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
					.codecs(JSONCodecResolver.default)
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
					.codecs(JSONCodecResolver.default)
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
					.codecs(JSONCodecResolver.default)
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
					.codecs(JSONCodecResolver.default)
					.destination(writer)
					.build()
					.apply {
						context.should.equal(testContext)
						writeBoolean(true)
						writer.toString().should.equal("true")
					}
			}
		}

		it(".writeEncodableOrNull()") {
			var expectedValue: Any? = null

			val encoder = object : DummyJSONEncoder() {
				override fun writeEncodable(value: Any) {
					value.should.equal(expectedValue)
				}

				override fun writeNull() {
					(null as Any?).should.equal(expectedValue)
				}
			}

			expectedValue = "okay"
			encoder.writeEncodableOrNull("okay")

			expectedValue = null
			encoder.writeEncodableOrNull(null)
		}

		it(".writeMapElement()") {
			var expectedKey: Any? = null
			var expectedValue: Any? = null

			val encoder = object : DummyJSONEncoder() {
				override fun writeMapKey(value: String) {
					(value as Any?).should.equal(expectedKey)
				}

				override fun writeEncodable(value: Any) {
					value.should.equal(expectedValue)
				}

				override fun writeNull() {
					(null as Any?).should.equal(expectedValue)
				}
			}

			expectedKey = "key"
			expectedValue = "value"
			encoder.writeMapElement(key = expectedKey, encodable = expectedValue!!)
			encoder.writeMapElement(key = expectedKey, encodable = expectedValue!!, skipIfNull = false)
			encoder.writeMapElement(key = expectedKey, encodable = expectedValue, skipIfNull = true)

			expectedValue = null
			encoder.writeMapElement(key = expectedKey, encodable = expectedValue, skipIfNull = false)

			expectedKey = null
			encoder.writeMapElement(key = "none", encodable = expectedValue, skipIfNull = true)
		}

		it(".writeValue[OrNull]()") {
			val expectedValue = "okay"

			val encoder = object : DummyJSONEncoder() {
				override fun writeEncodable(value: Any) {
					value.should.equal(expectedValue)
				}
			}

			encoder.writeValue("okay")
			encoder.writeValueAsMapKey("okay")

			try {
				encoder.writeValueAsMapKey(null)
				throw AssertionError("JSONEncoder.writeValueAsMapKey(null) should throw an exception")
			}
			catch (e: JSONException) {
				// good
			}
		}
	}
})
