package com.github.fluidsonic.fluid.json


object LongJSONCodec : AbstractJSONCodec<Long, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Long>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readLong()


	override fun encode(value: Long, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeLong(value)
}
