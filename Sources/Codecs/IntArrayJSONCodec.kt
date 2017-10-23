package com.github.fluidsonic.fluid.json


object IntArrayJSONCodec : JSONEncoderCodec<IntArray, JSONCoderContext> {

	override fun encode(value: IntArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(IntArray::class)
}
