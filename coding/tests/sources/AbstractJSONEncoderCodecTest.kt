package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


internal object AbstractJSONEncoderCodecTest {

	@Test
	fun testDecoderCodecForClassReturnsNested() {
		assert(OuterEncoderCodec.decoderCodecForType<Unit, JSONCodingContext>()).toBe(InnerDecoderCodec)
	}


	private object InnerDecoderCodec : AbstractJSONDecoderCodec<Unit, JSONCodingContext>() {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Unit>) =
			Unit
	}


	private object OuterEncoderCodec : AbstractJSONEncoderCodec<String, JSONCodingContext>(
		additionalProviders = listOf(InnerDecoderCodec)
	) {

		override fun JSONEncoder<JSONCodingContext>.encode(value: String) {}
	}
}
