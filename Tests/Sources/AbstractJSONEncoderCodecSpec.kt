package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


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
