package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : AbstractJSONEncoderCodec<ShortArray, JSONCoderContext>() {

	override fun encode(value: ShortArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)
}
