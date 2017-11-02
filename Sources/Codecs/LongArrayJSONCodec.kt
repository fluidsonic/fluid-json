package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : AbstractJSONEncoderCodec<LongArray, JSONCoderContext>() {

	override fun encode(value: LongArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)
}
