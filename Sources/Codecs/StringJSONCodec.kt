package com.github.fluidsonic.fluid.json


object StringJSONCodec : JSONCodec<String, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString()


	override fun encode(value: String, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value)


	override val valueClass = String::class.java
}
