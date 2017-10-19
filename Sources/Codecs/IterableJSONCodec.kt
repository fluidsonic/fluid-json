package com.github.fluidsonic.fluid.json


object IterableJSONCodec : JSONCodec<Iterable<*>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>): Iterable<*> =
		decoder.readList()


	override fun encode(value: Iterable<*>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = Iterable::class.java
}
