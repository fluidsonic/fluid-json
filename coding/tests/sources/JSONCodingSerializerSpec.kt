package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object JSONCodingSerializerSpec : Spek({

	describe("JSONCodingSerializer") {

		it(".builder()") {
			JSONCodingSerializer.builder()
				.encodingWith(BooleanJSONCodec)
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}

			JSONCodingSerializer.builder()
				.encodingWith(listOf(BooleanJSONCodec))
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}

			val testContext = TestCoderContext()

			JSONCodingSerializer.builder(testContext)
				.encodingWith()
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}
		}

		it(".builder() doesn't add standard codecs if different base is provided") {
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

		it(".default") {
			anyData.testEncoding(JSONCodingSerializer.default::serializeValue)
		}
	}
})