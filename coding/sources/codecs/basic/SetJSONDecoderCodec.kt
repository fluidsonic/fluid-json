package com.github.fluidsonic.fluid.json


object SetJSONDecoderCodec : AbstractJSONDecoderCodec<Set<*>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Set<*>>): Set<*> {
		val elementType = valueType.arguments.single()

		return mutableSetOf<Any?>().also { set ->
			readFromListByElement {
				set += readValueOfTypeOrNull(elementType)
			}
		}
	}


	val nonRecursive: JSONDecoderCodec<Set<*>, JSONCodingContext> = NonRecursiveJSONDecoderCodec.create()
}
