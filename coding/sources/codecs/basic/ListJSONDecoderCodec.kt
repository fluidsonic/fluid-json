package com.github.fluidsonic.fluid.json


object ListJSONDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<List<*>>): List<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}
	}


	val nonRecursive = NonRecursiveJSONDecoderCodec.create<List<*>>()
}
