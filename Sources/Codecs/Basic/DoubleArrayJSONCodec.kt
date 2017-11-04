package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : AbstractJSONEncoderCodec<DoubleArray, JSONCoderContext>() {

	override fun encode(value: DoubleArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)
}
