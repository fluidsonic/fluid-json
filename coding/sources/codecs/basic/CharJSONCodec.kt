package com.github.fluidsonic.fluid.json


object CharJSONCodec : AbstractJSONCodec<Char, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Char>) =
		readChar()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Char) =
		writeChar(value)
}
