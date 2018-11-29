package com.github.fluidsonic.fluid.json


object ShortJSONCodec : AbstractJSONCodec<Short, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Short>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readShort()


	override fun encode(value: Short, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeShort(value)
}
