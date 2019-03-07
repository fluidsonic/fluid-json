package com.github.fluidsonic.fluid.json


object StringJSONCodec : AbstractJSONCodec<String, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<String>) =
		readString()


	override fun JSONEncoder<JSONCodingContext>.encode(value: String) =
		writeString(value)
}
