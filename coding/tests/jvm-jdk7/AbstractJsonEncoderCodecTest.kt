package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


class AbstractJsonEncoderCodecTest {

	@Test
	fun testDecoderCodecForClassReturnsNested() {
		expect(OuterEncoderCodec.decoderCodecForType<Unit, JsonCodingContext>()).toBe(InnerDecoderCodec)
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
