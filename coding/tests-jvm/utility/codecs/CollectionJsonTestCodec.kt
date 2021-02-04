package tests.coding

import io.fluidsonic.json.*


internal object CollectionJsonTestCodec : AbstractJsonCodec<Collection<*>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, StringJsonCodec)
) {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Collection<*>>) =
		CollectionJsonCodec.run { decode(valueType) }


	override fun JsonEncoder<JsonCodingContext>.encode(value: Collection<*>) =
		CollectionJsonCodec.run { encode(value) }


	object NonRecursive : AbstractJsonCodec<Collection<*>, JsonCodingContext>(
		additionalProviders = listOf(StringJsonCodec)
	) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Collection<*>>) =
			CollectionJsonCodec.nonRecursive.run { decode(valueType) }


		override fun JsonEncoder<JsonCodingContext>.encode(value: Collection<*>) =
			CollectionJsonCodec.nonRecursive.run { encode(value) }
	}
}
