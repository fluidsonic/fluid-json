package com.github.fluidsonic.fluid.json


object BooleanArrayJSONCodec : JSONEncoderCodec<BooleanArray, JSONCoderContext> {

	override fun encode(value: BooleanArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(BooleanArray::class.java)
}
