package com.github.fluidsonic.fluid.json


object CharJSONCodec : AbstractJSONCodec<Char, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Char>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readChar()


	override fun encode(value: Char, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeChar(value)
}
