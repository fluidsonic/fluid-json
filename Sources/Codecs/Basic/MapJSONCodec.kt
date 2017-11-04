package com.github.fluidsonic.fluid.json


object MapJSONCodec : AbstractJSONCodec<Map<*, *>, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Map<*, *>>, decoder: JSONDecoder<JSONCoderContext>): Map<*, *> {
		val (elementKeyType, elementValueType) = valueType.arguments

		return decoder.readMapByElement {
			readValueOfTypeOrNull(elementKeyType) to readValueOfTypeOrNull(elementValueType)
		}
	}


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeMap(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Map<String, *>>()
}
