package tests

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

	private object InnerEncoderCodec : AbstractJSONEncoderCodec<Unit, JSONCoderContext>() {

		override fun encode(value: Unit, encoder: JSONEncoder<JSONCoderContext>) {}
	}


	private object OuterDecoderCodec : AbstractJSONDecoderCodec<String, JSONCoderContext>(
		additionalProviders = listOf(InnerEncoderCodec)
	) {

		override fun decode(valueType: JSONCodableType<in String>, decoder: JSONDecoder<JSONCoderContext>) =
			""
	}
}
