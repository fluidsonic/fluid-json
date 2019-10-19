package io.fluidsonic.json


object MapJsonCodec : AbstractJsonCodec<Map<*, *>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Map<*, *>>): Map<*, *> {
		val (elementKeyType, elementValueType) = valueType.arguments

		return readMapByElement {
			readValueOfTypeOrNull(elementKeyType) to readValueOfTypeOrNull(elementValueType)
		}
	}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Map<*, *>) =
		writeMap(value)


	val nonRecursive = NonRecursiveJsonCodec.create<Map<String, *>>()
}
