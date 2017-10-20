package com.github.fluidsonic.fluid.json


object IntJSONCodec : JSONCodec<Int, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readInt()


	override fun encode(value: Int, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeInt(value)


	override val valueClass = Int::class.java
}
