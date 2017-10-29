package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : AbstractJSONCodec<Double, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Double>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readDouble()


	override fun encode(value: Double, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeDouble(value)
}
