package com.github.fluidsonic.fluid.json


object NumberJSONCodec : JSONCodec<Number, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readNumber()


	override fun encode(value: Number, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeNumber(value)


	override val valueClass = Number::class.java
}
