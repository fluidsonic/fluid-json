package io.fluidsonic.json


object SetJsonDecoderCodec : AbstractJsonDecoderCodec<Set<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Set<*>>): Set<*> {
		val elementType = valueType.arguments.single()

		return mutableSetOf<Any?>().also { set ->
			readFromListByElement {
				set += readValueOfTypeOrNull(elementType)
			}
		}
	}


	val NON_RECURSIVE: JsonDecoderCodec<Set<*>, JsonCodingContext> = NonRecursiveJsonDecoderCodec.create()
}
