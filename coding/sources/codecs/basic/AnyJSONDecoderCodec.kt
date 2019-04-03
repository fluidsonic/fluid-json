package com.github.fluidsonic.fluid.json


object AnyJSONDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Any>): Any =
		when (nextToken) {
			JSONToken.booleanValue -> readValueOfType<Boolean>()
			JSONToken.listStart -> readValueOfType<List<*>>()
			JSONToken.mapKey -> readValueOfType<String>()
			JSONToken.mapStart -> readValueOfType<Map<*, *>>()
			JSONToken.numberValue -> readValueOfType<Number>()
			JSONToken.stringValue -> readValueOfType<String>()
			else -> invalidValueError("unexpected 'null'")
		}
}
