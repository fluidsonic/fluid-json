package com.github.fluidsonic.fluid.json


object CharArrayJSONCodec : AbstractJSONEncoderCodec<CharArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: CharArray) =
		writeList(value)
}
