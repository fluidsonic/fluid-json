package com.github.fluidsonic.fluid.json


object NumberJSONCodec : JSONCodec<Number, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readNumber()


	override fun encode(value: Number, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeNumber(value)


	override val decodableClass = Number::class.java
}
