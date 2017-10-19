package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : JSONEncoderCodec<DoubleArray, JSONCoderContext> {

	override fun encode(value: DoubleArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = DoubleArray::class.java
}
