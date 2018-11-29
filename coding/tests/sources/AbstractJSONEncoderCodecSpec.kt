package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object AbstractJSONEncoderCodecSpec : Spek({

	describe("AbstractJSONEncoderCodec") {

		it("returns nested decoder codecs") {
			OuterEncoderCodec.decoderCodecForType<Unit, JSONCodingContext>().should.equal(InnerDecoderCodec)
		}
	}
}) {

	private object InnerDecoderCodec : AbstractJSONDecoderCodec<Unit, JSONCodingContext>() {

		override fun decode(valueType: JSONCodingType<in Unit>, decoder: JSONDecoder<JSONCodingContext>) =
			Unit
	}


	private object OuterEncoderCodec : AbstractJSONEncoderCodec<String, JSONCodingContext>(
		additionalProviders = listOf(InnerDecoderCodec)
	) {

		override fun encode(value: String, encoder: JSONEncoder<JSONCodingContext>) {}
	}
}
