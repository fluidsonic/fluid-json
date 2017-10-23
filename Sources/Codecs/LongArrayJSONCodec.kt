package com.github.fluidsonic.fluid.json


object LongArrayJSONCodec : JSONEncoderCodec<LongArray, JSONCoderContext> {

	override fun encode(value: LongArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(LongArray::class)
}
