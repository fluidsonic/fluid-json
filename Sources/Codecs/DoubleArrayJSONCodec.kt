package com.github.fluidsonic.fluid.json


object DoubleArrayJSONCodec : JSONEncoderCodec<DoubleArray, JSONCoderContext> {

	override fun encode(value: DoubleArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(DoubleArray::class.java)
}
