package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : AbstractJSONEncoderCodec<ByteArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: ByteArray) =
		writeList(value)
}
