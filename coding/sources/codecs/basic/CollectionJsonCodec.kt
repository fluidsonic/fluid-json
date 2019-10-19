package io.fluidsonic.json


object CollectionJsonCodec : AbstractJsonCodec<Collection<*>, JsonCodingContext>() {

	@Suppress("UNCHECKED_CAST")
	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Collection<*>>): Collection<*> =
		ListJsonDecoderCodec.run { decode(valueType as JsonCodingType<List<*>>) }


	override fun JsonEncoder<JsonCodingContext>.encode(value: Collection<*>): Unit =
		writeList(value)


	val nonRecursive: JsonCodec<Collection<*>, JsonCodingContext> = NonRecursiveJsonCodec.create()
}
