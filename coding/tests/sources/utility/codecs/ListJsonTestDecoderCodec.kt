package tests.coding

import io.fluidsonic.json.*


internal object ListJsonTestDecoderCodec : AbstractJsonDecoderCodec<List<*>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, BooleanJsonCodec, NumberJsonCodec, StringJsonCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<List<*>>) =
		ListJsonDecoderCodec.run { decode(valueType) }


	object NonRecursive : AbstractJsonDecoderCodec<List<*>, JsonCodingContext>(
		additionalProviders = listOf(BooleanJsonCodec, IntJsonCodec, StringJsonCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<List<*>>) =
			ListJsonDecoderCodec.nonRecursive.run { decode(valueType) }
	}
}
