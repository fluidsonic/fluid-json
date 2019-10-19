package tests.coding

import io.fluidsonic.json.*


internal object SequenceJsonTestCodec : AbstractJsonCodec<Sequence<*>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, BooleanJsonCodec, IterableJsonEncoderCodec, ListJsonDecoderCodec, NumberJsonCodec, StringJsonCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Sequence<*>>) =
		SequenceJsonCodec.run { decode(valueType) }


	override fun JsonEncoder<JsonCodingContext>.encode(value: Sequence<*>) =
		SequenceJsonCodec.run { encode(value) }


	object NonRecursive : AbstractJsonCodec<Sequence<*>, JsonCodingContext>(
		additionalProviders = listOf(BooleanJsonCodec, IntJsonCodec, IterableJsonEncoderCodec, StringJsonCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Sequence<*>>) =
			SequenceJsonCodec.nonRecursive.run { decode(valueType) }


		override fun JsonEncoder<JsonCodingContext>.encode(value: Sequence<*>) =
			SequenceJsonCodec.nonRecursive.run { encode(value) }
	}
}
