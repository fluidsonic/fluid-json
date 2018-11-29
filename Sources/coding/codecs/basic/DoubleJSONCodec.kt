package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : AbstractJSONCodec<Double, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Double>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readDouble()


	override fun encode(value: Double, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeDouble(value)
}
