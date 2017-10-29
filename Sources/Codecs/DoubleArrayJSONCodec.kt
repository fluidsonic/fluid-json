package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : AbstractJSONEncoderCodec<DoubleArray, JSONCoderContext>() {

	override fun encode(value: DoubleArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
