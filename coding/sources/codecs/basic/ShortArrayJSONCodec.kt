package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : AbstractJSONEncoderCodec<ShortArray, JSONCodingContext>() {

	override fun encode(value: ShortArray, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)
}
