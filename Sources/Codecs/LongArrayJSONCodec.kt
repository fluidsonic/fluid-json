package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : JSONEncoderCodec<LongArray, JSONCoderContext> {

	override fun encode(value: LongArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = LongArray::class.java
}
