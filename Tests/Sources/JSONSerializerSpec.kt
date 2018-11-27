package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object JSONSerializerSpec : Spek({

	describe("JSONSerializer") {

		it(".builder()") {
			JSONSerializer.builder()
				.encodingWith(BooleanJSONCodec)
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}

			JSONSerializer.builder()
				.encodingWith(listOf(BooleanJSONCodec))
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}

			val testContext = TestCoderContext()

			JSONSerializer.builder(testContext)
				.encodingWith()
				.build()
				.apply {
					// TODO check correct context
					serializeValue(true).should.equal("true")
				}
		}

		it(".default") {
			anyData.testEncoding(JSONSerializer.default::serializeValue)
		}
	}
})
