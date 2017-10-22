package com.github.fluidsonic.fluid.json


object SequenceJSONCodec : JSONCodec<Sequence<*>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Sequence<*> =
		decoder.readList(JSONNullability.Value).asSequence()


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val decodableClass = Sequence::class.java
}
