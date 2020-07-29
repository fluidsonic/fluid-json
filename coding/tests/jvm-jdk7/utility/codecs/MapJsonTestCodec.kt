package tests.coding

import io.fluidsonic.json.*


internal object MapJsonTestCodec : AbstractJsonCodec<Map<*, *>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, BooleanJsonCodec, NumberJsonCodec, StringJsonCodec, YearMonthDayCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Map<*, *>>) =
		MapJsonCodec.run { decode(valueType) }


	override fun JsonEncoder<JsonCodingContext>.encode(value: Map<*, *>) =
		MapJsonCodec.run { encode(value) }


	object NonRecursive : AbstractJsonCodec<Map<String, *>, JsonCodingContext>(
		additionalProviders = listOf(BooleanJsonCodec, IntJsonCodec, StringJsonCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Map<String, *>>) =
			MapJsonCodec.nonRecursive.run { decode(valueType) }


		override fun JsonEncoder<JsonCodingContext>.encode(value: Map<String, *>) =
			MapJsonCodec.nonRecursive.run { encode(value) }
	}
}
