package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object AbstractJSONDecoderCodecSpec : Spek({

	describe("AbstractJSONDecoderCodec") {

		it("returns nested encoder codecs") {
			OuterDecoderCodec.encoderCodecForClass(Unit::class).should.equal(InnerEncoderCodec)
		}
	}
}) {

	private object InnerEncoderCodec : AbstractJSONEncoderCodec<Unit, JSONCodingContext>() {

		override fun encode(value: Unit, encoder: JSONEncoder<JSONCodingContext>) {}
	}


	private object OuterDecoderCodec : AbstractJSONDecoderCodec<String, JSONCodingContext>(
		additionalProviders = listOf(InnerEncoderCodec)
	) {

		override fun decode(valueType: JSONCodingType<in String>, decoder: JSONDecoder<JSONCodingContext>) =
			""
	}
}
