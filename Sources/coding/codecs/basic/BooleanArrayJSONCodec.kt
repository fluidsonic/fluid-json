package com.github.fluidsonic.fluid.json


object BooleanArrayJSONCodec : AbstractJSONEncoderCodec<BooleanArray, JSONCodingContext>() {

	override fun encode(value: BooleanArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
