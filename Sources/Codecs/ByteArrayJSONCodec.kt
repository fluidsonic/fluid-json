package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : AbstractJSONEncoderCodec<ByteArray, JSONCoderContext>() {

	override fun encode(value: ByteArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)
}
