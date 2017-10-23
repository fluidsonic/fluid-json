package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : JSONEncoderCodec<ByteArray, JSONCoderContext> {

	override fun encode(value: ByteArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(ByteArray::class)
}
