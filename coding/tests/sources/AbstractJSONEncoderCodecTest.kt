package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.notToBeNullBut
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


internal object AbstractJSONEncoderCodecTest {

	@Test
	fun testDecoderCodecForClassReturnsNested() {
		assert(OuterEncoderCodec.decoderCodecForType<Unit, JSONCodingContext>()).notToBeNullBut(InnerDecoderCodec)
	}


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
