package com.github.fluidsonic.fluid.json


object IntJSONCodec : AbstractJSONCodec<Int, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Int>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readInt()


	override fun encode(value: Int, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeInt(value)
}
