package com.github.fluidsonic.fluid.json


object LongJSONCodec : AbstractJSONCodec<Long, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Long>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readLong()


	override fun encode(value: Long, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeLong(value)
}
