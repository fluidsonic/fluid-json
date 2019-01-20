package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : AbstractJSONEncoderCodec<DoubleArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: DoubleArray) =
		writeList(value)
}
