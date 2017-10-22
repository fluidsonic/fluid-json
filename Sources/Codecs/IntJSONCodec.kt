package com.github.fluidsonic.fluid.json


object IntJSONCodec : JSONCodec<Int, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readInt()


	override fun encode(value: Int, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeInt(value)


	override val decodableClass = Int::class.java
}
