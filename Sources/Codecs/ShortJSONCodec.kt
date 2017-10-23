package com.github.fluidsonic.fluid.json


object ShortJSONCodec : JSONCodec<Short, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readShort()


	override fun encode(value: Short, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeShort(value)


	override val decodableClass = Short::class
}
