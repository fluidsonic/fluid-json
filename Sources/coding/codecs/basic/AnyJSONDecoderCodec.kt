package com.github.fluidsonic.fluid.json


object AnyJSONDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Any>, decoder: JSONDecoder<JSONCodingContext>): Any =
		when (decoder.nextToken) {
			JSONToken.booleanValue -> decoder.readValueOfType<Boolean>()
			JSONToken.listStart -> decoder.readValueOfType<List<*>>()
			JSONToken.mapKey -> decoder.readValueOfType<String>()
			JSONToken.mapStart -> decoder.readValueOfType<Map<*, *>>()
			JSONToken.numberValue -> decoder.readValueOfType<Number>()
			JSONToken.stringValue -> decoder.readValueOfType<String>()
			else -> error("impossible token: ${decoder.nextToken}")
		}
}
