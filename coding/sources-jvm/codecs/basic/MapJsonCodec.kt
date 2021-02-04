package io.fluidsonic.json


public object MapJsonCodec : AbstractJsonCodec<Map<*, *>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Map<*, *>>): Map<*, *> {
		val (elementKeyType, elementValueType) = valueType.arguments

		return readMapByElement {
			readValueOfTypeOrNull(elementKeyType) to readValueOfTypeOrNull(elementValueType)
		}
	}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Map<*, *>): Unit =
		writeMap(value)


	public val nonRecursive: JsonCodec<Map<String, *>, JsonCodingContext> = NonRecursiveJsonCodec.create()
}
