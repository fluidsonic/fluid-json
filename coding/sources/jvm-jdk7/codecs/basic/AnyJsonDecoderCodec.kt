package io.fluidsonic.json


public object AnyJsonDecoderCodec : AbstractJsonDecoderCodec<Any, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Any>): Any =
		when (nextToken) {
			JsonToken.booleanValue -> readValueOfType<Boolean>()
			JsonToken.listStart -> readValueOfType<List<*>>()
			JsonToken.mapKey -> readValueOfType<String>()
			JsonToken.mapStart -> readValueOfType<Map<*, *>>()
			JsonToken.numberValue -> readValueOfType<Number>()
			JsonToken.stringValue -> readValueOfType<String>()
			else -> invalidValueError("unexpected 'null'")
		}
}
