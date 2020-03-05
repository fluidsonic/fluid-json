package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object AbstractJsonDecoderCodecTest {

	@Test
	fun testEncoderCodecForClassReturnsNested() {
		expect(OuterDecoderCodec.encoderCodecForClass(Unit::class)).toBe(InnerEncoderCodec)
	}


	private object InnerEncoderCodec : AbstractJsonEncoderCodec<Unit, JsonCodingContext>() {

		override fun JsonEncoder<JsonCodingContext>.encode(value: Unit) {}
	}


	private object OuterDecoderCodec : AbstractJsonDecoderCodec<String, JsonCodingContext>(
		additionalProviders = listOf(InnerEncoderCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<String>) =
			""
	}
}
