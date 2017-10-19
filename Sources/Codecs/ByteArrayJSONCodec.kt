package com.github.fluidsonic.fluid.json


object ByteArrayJSONCodec : JSONEncoderCodec<ByteArray, JSONCoderContext> {

	override fun encode(value: ByteArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = ByteArray::class.java
}
