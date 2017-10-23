package com.github.fluidsonic.fluid.json


object IterableJSONCodec : JSONCodec<Iterable<*>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Iterable<*> =
		decoder.readList(JSONNullability.Value)


	override fun encode(value: Iterable<*>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val decodableClass = Iterable::class
}
