package com.github.fluidsonic.fluid.json


object MapJSONCodec : AbstractJSONCodec<Map<*, *>, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Map<*, *>>, decoder: JSONDecoder<JSONCodingContext>): Map<*, *> {
		val (elementKeyType, elementValueType) = valueType.arguments

		return decoder.readMapByElement {
			readValueOfTypeOrNull(elementKeyType) to readValueOfTypeOrNull(elementValueType)
		}
	}


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeMap(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Map<String, *>>()
}
