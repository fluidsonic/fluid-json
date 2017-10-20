package com.github.fluidsonic.fluid.json


object ByteJSONCodec : JSONCodec<Byte, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readByte()


	override fun encode(value: Byte, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeByte(value)


	override val valueClass = Byte::class.java
}
