package com.github.fluidsonic.fluid.json


object MapJSONCodec : AbstractJSONCodec<Map<*, *>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Map<*, *>>): Map<*, *> {
		val (elementKeyType, elementValueType) = valueType.arguments

		return readMapByElement {
			readValueOfTypeOrNull(elementKeyType) to readValueOfTypeOrNull(elementValueType)
		}
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Map<*, *>) =
		writeMap(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Map<String, *>>()
}
