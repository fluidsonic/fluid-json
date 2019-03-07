package com.github.fluidsonic.fluid.json


object IntJSONCodec : AbstractJSONCodec<Int, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Int>) =
		readInt()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Int) =
		writeInt(value)
}
