package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object AbstractJsonEncoderCodecTest {

	@Test
	fun testDecoderCodecForClassReturnsNested() {
		assert(OuterEncoderCodec.decoderCodecForType<Unit, JsonCodingContext>()).toBe(InnerDecoderCodec)
	}


	private object InnerDecoderCodec : AbstractJsonDecoderCodec<Unit, JsonCodingContext>() {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Unit>) =
			Unit
	}


	private object OuterEncoderCodec : AbstractJsonEncoderCodec<String, JsonCodingContext>(
		additionalProviders = listOf(InnerDecoderCodec)
	) {

		override fun JsonEncoder<JsonCodingContext>.encode(value: String) {}
	}
}
