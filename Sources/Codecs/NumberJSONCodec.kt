package com.github.fluidsonic.fluid.json


object NumberJSONCodec : AbstractJSONCodec<Number, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Number>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readNumber()


	override fun encode(value: Number, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeNumber(value)
}
