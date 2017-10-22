package com.github.fluidsonic.fluid.json


object ByteJSONCodec : JSONCodec<Byte, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readByte()


	override fun encode(value: Byte, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeByte(value)


	override val decodableClass = Byte::class.java
}
