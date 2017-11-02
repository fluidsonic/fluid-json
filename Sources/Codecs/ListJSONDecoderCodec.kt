package com.github.fluidsonic.fluid.json


object ListJSONDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in List<*>>, decoder: JSONDecoder<JSONCoderContext>): List<*> {
		val elementType = valueType.arguments.single()

		return decoder.readListByElement {
			readValueOfTypeOrNull(elementType)
		}
	}


	val nonRecursive = NonRecursiveJSONDecoderCodec.create<List<*>>()
}
