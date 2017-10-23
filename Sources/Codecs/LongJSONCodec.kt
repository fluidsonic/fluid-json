package com.github.fluidsonic.fluid.json


object LongJSONCodec : JSONCodec<Long, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readLong()


	override fun encode(value: Long, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeLong(value)


	override val decodableClass = Long::class
}
