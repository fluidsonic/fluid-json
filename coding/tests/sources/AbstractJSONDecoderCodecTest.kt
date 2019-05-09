package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


internal object AbstractJSONDecoderCodecTest {

	@Test
	fun testEncoderCodecForClassReturnsNested() {
		assert(OuterDecoderCodec.encoderCodecForClass(Unit::class)).toBe(InnerEncoderCodec)
	}


	private object InnerEncoderCodec : AbstractJSONEncoderCodec<Unit, JSONCodingContext>() {

		override fun JSONEncoder<JSONCodingContext>.encode(value: Unit) {}
	}


	private object OuterDecoderCodec : AbstractJSONDecoderCodec<String, JSONCodingContext>(
		additionalProviders = listOf(InnerEncoderCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<String>) =
			""
	}
}
