package io.fluidsonic.json


public object SetJsonDecoderCodec : AbstractJsonDecoderCodec<Set<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Set<*>>): Set<*> {
		val elementType = valueType.arguments.single()

		return mutableSetOf<Any?>().also { set ->
			readFromListByElement {
				set += readValueOfTypeOrNull(elementType)
			}
		}
	}


	public val nonRecursive: JsonDecoderCodec<Set<*>, JsonCodingContext> = NonRecursiveJsonDecoderCodec.create()
}
