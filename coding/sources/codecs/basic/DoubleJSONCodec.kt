package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : AbstractJSONCodec<Double, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Double>) =
		readDouble()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Double) =
		writeDouble(value)
}
