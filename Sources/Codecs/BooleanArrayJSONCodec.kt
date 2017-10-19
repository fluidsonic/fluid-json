package com.github.fluidsonic.fluid.json


object BooleanArrayJSONCodec : JSONEncoderCodec<BooleanArray, JSONCoderContext> {

	override fun encode(value: BooleanArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = BooleanArray::class.java
}
