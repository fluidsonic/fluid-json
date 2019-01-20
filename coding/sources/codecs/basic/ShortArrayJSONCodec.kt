package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : AbstractJSONEncoderCodec<ShortArray, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: ShortArray) =
		writeList(value)
}
