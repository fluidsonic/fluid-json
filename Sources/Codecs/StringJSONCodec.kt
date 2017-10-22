package com.github.fluidsonic.fluid.json


object StringJSONCodec : JSONCodec<String, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readString()


	override fun encode(value: String, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeString(value)


	override val decodableClass = String::class.java
}
