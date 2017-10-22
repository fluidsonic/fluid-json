package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.serializeValue
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object JSONSerializerSpec : Spek({

	describe("JSONSerializer") {

		it(".builder()") {
			JSONSerializer.builder()
				.encoder(JSONCodecResolver.default)
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					serializeValue(true).should.equal("true")
				}

			JSONSerializer.builder()
				.encoder(BooleanJSONCodec)
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					serializeValue(true).should.equal("true")
				}

			JSONSerializer.builder()
				.encoder(listOf(BooleanJSONCodec))
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					serializeValue(true).should.equal("true")
				}

			val testContext = TestCoderContext()

			JSONSerializer.builder(testContext)
				.encoder(JSONCodecResolver.default)
				.build()
				.apply {
					context.should.equal(testContext)
					serializeValue(true).should.equal("true")
				}
		}

		it(".default()") {
			anyData.testEncoding(JSONSerializer.default()::serializeValue)
		}

		it(".withContext()") {
			val testContext = TestCoderContext()

			JSONSerializer.default()
				.withContext(testContext)
				.context.should.equal(testContext)
		}
	}
})
