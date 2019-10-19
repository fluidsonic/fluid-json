package io.fluidsonic.json


object SequenceJsonCodec : AbstractJsonCodec<Sequence<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Sequence<*>>): Sequence<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}.asSequence()
	}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Sequence<*>) =
		writeList(value)


	val nonRecursive = NonRecursiveJsonCodec.create<Sequence<*>>()
}
