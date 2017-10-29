package com.github.fluidsonic.fluid.json


object ByteJSONCodec : AbstractJSONCodec<Byte, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Byte>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readByte()


	override fun encode(value: Byte, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeByte(value)
}
