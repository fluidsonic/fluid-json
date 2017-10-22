package com.github.fluidsonic.fluid.json


object FloatArrayJSONCodec : JSONEncoderCodec<FloatArray, JSONCoderContext> {

	override fun encode(value: FloatArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(FloatArray::class.java)
}
