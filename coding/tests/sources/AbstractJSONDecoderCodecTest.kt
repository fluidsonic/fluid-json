package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.notToBeNullBut
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


internal object AbstractJSONDecoderCodecTest {

	@Test
	fun testEncoderCodecForClassReturnsNested() {
		assert(OuterDecoderCodec.encoderCodecForClass(Unit::class)).notToBeNullBut(InnerEncoderCodec)
	}


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
