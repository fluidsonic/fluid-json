package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : AbstractJSONEncoderCodec<IntArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: IntArray) =
		writeList(value)
}
