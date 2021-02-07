package io.fluidsonic.json


public object ListJsonDecoderCodec : AbstractJsonDecoderCodec<List<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<List<*>>): List<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			// No readValueOfTypeOrNull() to reduce stack size for deep recursive structures.
			readOrNull { readValueOfType(elementType) }
		}
	}


	public val nonRecursive: JsonDecoderCodec<List<*>, JsonCodingContext> = NonRecursiveJsonDecoderCodec.create()
}
