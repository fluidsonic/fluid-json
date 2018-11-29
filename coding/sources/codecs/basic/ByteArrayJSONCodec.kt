package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : AbstractJSONEncoderCodec<ByteArray, JSONCodingContext>() {

	override fun encode(value: ByteArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
