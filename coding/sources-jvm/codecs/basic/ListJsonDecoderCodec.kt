package io.fluidsonic.json


public object ListJsonDecoderCodec : AbstractJsonDecoderCodec<List<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<List<*>>): List<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}
	}


	public val nonRecursive: JsonDecoderCodec<List<*>, JsonCodingContext> = NonRecursiveJsonDecoderCodec.create()
}
