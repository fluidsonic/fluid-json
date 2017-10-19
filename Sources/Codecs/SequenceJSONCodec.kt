package com.github.fluidsonic.fluid.json


object SequenceJSONCodec : JSONCodec<Sequence<*>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>): Sequence<*> =
		decoder.readList().asSequence()


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = Sequence::class.java
}
