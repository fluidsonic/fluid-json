package com.github.fluidsonic.fluid.json


object ByteJSONCodec : AbstractJSONCodec<Byte, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Byte>) =
		readByte()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Byte) =
		writeByte(value)
}
