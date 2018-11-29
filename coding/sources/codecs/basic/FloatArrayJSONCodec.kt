package com.github.fluidsonic.fluid.json


object FloatArrayJSONCodec : AbstractJSONEncoderCodec<FloatArray, JSONCodingContext>() {

	override fun encode(value: FloatArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
