package tests.coding

import io.fluidsonic.json.*


internal object AnyJsonTestDecoderCodec : AbstractJsonDecoderCodec<Any, JsonCodingContext>(
	additionalProviders = listOf(BooleanJsonCodec, ListJsonDecoderCodec, MapJsonCodec, NumberJsonCodec, StringJsonCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Any>) =
		AnyJsonDecoderCodec.run { decode(valueType) }
}
