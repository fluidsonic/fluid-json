package com.github.fluidsonic.fluid.json


object CharArrayJSONCodec : AbstractJSONEncoderCodec<CharArray, JSONCodingContext>() {

	override fun encode(value: CharArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
