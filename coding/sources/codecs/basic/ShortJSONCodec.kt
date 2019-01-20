package com.github.fluidsonic.fluid.json


object ShortJSONCodec : AbstractJSONCodec<Short, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Short>) =
		readShort()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Short) =
		writeShort(value)
}
