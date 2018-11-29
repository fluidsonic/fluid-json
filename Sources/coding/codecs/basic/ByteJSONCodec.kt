package com.github.fluidsonic.fluid.json


object ByteJSONCodec : AbstractJSONCodec<Byte, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Byte>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readByte()


	override fun encode(value: Byte, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeByte(value)
}
