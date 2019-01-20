package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : AbstractJSONEncoderCodec<LongArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: LongArray) =
		writeList(value)
}
