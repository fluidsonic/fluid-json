package com.github.fluidsonic.fluid.json


object ShortJSONCodec : AbstractJSONCodec<Short, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Short>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readShort()


	override fun encode(value: Short, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeShort(value)
}
