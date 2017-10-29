package com.github.fluidsonic.fluid.json


object ShortJSONCodec : AbstractJSONCodec<Short, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Short>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readShort()


	override fun encode(value: Short, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeShort(value)
}
