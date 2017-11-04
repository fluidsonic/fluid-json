package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


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
