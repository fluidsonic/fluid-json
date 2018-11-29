package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : AbstractJSONEncoderCodec<IntArray, JSONCodingContext>() {

	override fun encode(value: IntArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
