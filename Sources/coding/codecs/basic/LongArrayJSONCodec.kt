package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : AbstractJSONEncoderCodec<LongArray, JSONCodingContext>() {

	override fun encode(value: LongArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
