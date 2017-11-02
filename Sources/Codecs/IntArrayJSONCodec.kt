package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : AbstractJSONEncoderCodec<IntArray, JSONCoderContext>() {

	override fun encode(value: IntArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)
}
