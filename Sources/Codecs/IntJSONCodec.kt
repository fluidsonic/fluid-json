package com.github.fluidsonic.fluid.json


object IntJSONCodec : AbstractJSONCodec<Int, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Int>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readInt()


	override fun encode(value: Int, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeInt(value)
}
