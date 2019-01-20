package com.github.fluidsonic.fluid.json


object BooleanArrayJSONCodec : AbstractJSONEncoderCodec<BooleanArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: BooleanArray) =
		writeList(value)
}
