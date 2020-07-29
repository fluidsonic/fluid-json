package tests.coding

import io.fluidsonic.json.*


internal object SetJsonTestDecoderCodec : AbstractJsonDecoderCodec<Set<*>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, BooleanJsonCodec, ListJsonDecoderCodec, NumberJsonCodec, StringJsonCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Set<*>>) =
		SetJsonDecoderCodec.run { decode(valueType) }


	object NonRecursive : AbstractJsonDecoderCodec<Set<*>, JsonCodingContext>(
		additionalProviders = listOf(BooleanJsonCodec, NumberJsonCodec, StringJsonCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Set<*>>) =
			SetJsonDecoderCodec.nonRecursive.run { decode(valueType) }
	}
}
