package com.github.fluidsonic.fluid.json


object ShortJSONCodec : JSONCodec<Short, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readShort()


	override fun encode(value: Short, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeShort(value)


	override val valueClass = Short::class.java
}
