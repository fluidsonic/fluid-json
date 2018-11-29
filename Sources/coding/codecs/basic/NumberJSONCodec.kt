package com.github.fluidsonic.fluid.json


object NumberJSONCodec : AbstractJSONCodec<Number, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Number>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readNumber()


	override fun encode(value: Number, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeNumber(value)
}
