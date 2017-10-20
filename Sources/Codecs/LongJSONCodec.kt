package com.github.fluidsonic.fluid.json


object LongJSONCodec : JSONCodec<Long, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readLong()


	override fun encode(value: Long, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeLong(value)


	override val valueClass = Long::class.java
}
