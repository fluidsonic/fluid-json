package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : JSONEncoderCodec<IntArray, JSONCoderContext> {

	override fun encode(value: IntArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = IntArray::class.java
}
