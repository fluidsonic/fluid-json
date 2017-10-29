package com.github.fluidsonic.fluid.json


object FloatJSONCodec : AbstractJSONCodec<Float, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Float>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readFloat()


	override fun encode(value: Float, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeFloat(value)
}
