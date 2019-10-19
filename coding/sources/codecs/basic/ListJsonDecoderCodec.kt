package io.fluidsonic.json


object ListJsonDecoderCodec : AbstractJsonDecoderCodec<List<*>, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<List<*>>): List<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}
	}


	val nonRecursive = NonRecursiveJsonDecoderCodec.create<List<*>>()
}
