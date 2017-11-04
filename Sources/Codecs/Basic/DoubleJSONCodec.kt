package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : AbstractJSONCodec<Double, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Double>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readDouble()


	override fun encode(value: Double, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeDouble(value)
}
