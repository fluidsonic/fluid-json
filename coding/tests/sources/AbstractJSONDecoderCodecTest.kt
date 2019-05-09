package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


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
