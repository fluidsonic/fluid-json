package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : AbstractJSONEncoderCodec<LongArray, JSONCoderContext>() {

	override fun encode(value: LongArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
