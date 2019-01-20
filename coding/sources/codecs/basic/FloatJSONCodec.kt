package com.github.fluidsonic.fluid.json


object FloatJSONCodec : AbstractJSONCodec<Float, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Float>) =
		readFloat()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Float) =
		writeFloat(value)
}
