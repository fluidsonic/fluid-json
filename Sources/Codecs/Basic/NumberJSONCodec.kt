package com.github.fluidsonic.fluid.json


object NumberJSONCodec : AbstractJSONCodec<Number, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Number>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readNumber()


	override fun encode(value: Number, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeNumber(value)
}
