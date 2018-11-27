package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object AbstractJSONEncoderCodecSpec : Spek({

	describe("AbstractJSONEncoderCodec") {

		it("returns nested decoder codecs") {
			OuterEncoderCodec.decoderCodecForType<Unit, JSONCoderContext>().should.equal(InnerDecoderCodec)
		}
	}
}) {

	private object InnerDecoderCodec : AbstractJSONDecoderCodec<Unit, JSONCoderContext>() {

		override fun decode(valueType: JSONCodableType<in Unit>, decoder: JSONDecoder<JSONCoderContext>) =
			Unit
	}


	private object OuterEncoderCodec : AbstractJSONEncoderCodec<String, JSONCoderContext>(
		additionalProviders = listOf(InnerDecoderCodec)
	) {

		override fun encode(value: String, encoder: JSONEncoder<JSONCoderContext>) {}
	}
}
