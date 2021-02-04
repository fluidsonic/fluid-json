package io.fluidsonic.json


public object SequenceJsonCodec : AbstractJsonCodec<Sequence<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Sequence<*>>): Sequence<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}.asSequence()
	}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Sequence<*>): Unit =
		writeList(value)


	public val nonRecursive: JsonCodec<Sequence<*>, JsonCodingContext> = NonRecursiveJsonCodec.create()
}
