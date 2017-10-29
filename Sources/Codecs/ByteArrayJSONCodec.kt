package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : AbstractJSONEncoderCodec<ByteArray, JSONCoderContext>() {

	override fun encode(value: ByteArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
