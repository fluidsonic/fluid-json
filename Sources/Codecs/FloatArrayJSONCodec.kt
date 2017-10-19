package com.github.fluidsonic.fluid.json


object FloatArrayJSONCodec : JSONEncoderCodec<FloatArray, JSONCoderContext> {

	override fun encode(value: FloatArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = FloatArray::class.java
}
