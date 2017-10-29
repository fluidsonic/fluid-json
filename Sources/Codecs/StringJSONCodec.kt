package com.github.fluidsonic.fluid.json


object StringJSONCodec : AbstractJSONCodec<String, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in String>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readString()


	override fun encode(value: String, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeString(value)
}
