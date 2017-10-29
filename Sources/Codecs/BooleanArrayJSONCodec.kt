package com.github.fluidsonic.fluid.json


object BooleanArrayJSONCodec : AbstractJSONEncoderCodec<BooleanArray, JSONCoderContext>() {

	override fun encode(value: BooleanArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
