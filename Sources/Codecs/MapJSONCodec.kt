package com.github.fluidsonic.fluid.json


object MapJSONCodec : AbstractJSONCodec<Map<*, *>, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Map<*, *>>, decoder: JSONDecoder<out JSONCoderContext>): Map<*, *> {
		val (keyType, valueType) = valueType.arguments

		return decoder.readMapByElement {
			readValueOfTypeOrNull(keyType) to readValueOfTypeOrNull(valueType)
		}
	}


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeMap(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Map<String, *>>()
}
