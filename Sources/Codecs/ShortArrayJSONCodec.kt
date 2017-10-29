package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : AbstractJSONEncoderCodec<ShortArray, JSONCoderContext>() {

	override fun encode(value: ShortArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)
}
