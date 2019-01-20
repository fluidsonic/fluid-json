package com.github.fluidsonic.fluid.json


object LongJSONCodec : AbstractJSONCodec<Long, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Long>) =
		readLong()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Long) =
		writeLong(value)
}
