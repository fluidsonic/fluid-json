package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : AbstractJSONEncoderCodec<IntArray, JSONCoderContext>() {

	override fun encode(value: IntArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
