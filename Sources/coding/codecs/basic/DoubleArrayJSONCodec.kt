package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : AbstractJSONEncoderCodec<DoubleArray, JSONCodingContext>() {

	override fun encode(value: DoubleArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
