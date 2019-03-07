package com.github.fluidsonic.fluid.json


object NumberJSONCodec : AbstractJSONCodec<Number, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Number>) =
		readNumber()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Number) =
		writeNumber(value)
}
