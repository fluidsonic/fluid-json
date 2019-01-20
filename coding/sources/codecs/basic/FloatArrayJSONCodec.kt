package com.github.fluidsonic.fluid.json


object FloatArrayJSONCodec : AbstractJSONEncoderCodec<FloatArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: FloatArray) =
		writeList(value)
}
