package com.github.fluidsonic.fluid.json


object StringJSONCodec : AbstractJSONCodec<String, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in String>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString()


	override fun encode(value: String, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value)
}
