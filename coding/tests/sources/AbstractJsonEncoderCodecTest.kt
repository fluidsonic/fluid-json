package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object AbstractJsonEncoderCodecTest {

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
