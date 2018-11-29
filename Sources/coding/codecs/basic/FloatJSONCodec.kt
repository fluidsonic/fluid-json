package com.github.fluidsonic.fluid.json


object FloatJSONCodec : AbstractJSONCodec<Float, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Float>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readFloat()


	override fun encode(value: Float, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeFloat(value)
}
