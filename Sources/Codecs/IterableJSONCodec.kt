package com.github.fluidsonic.fluid.json


internal class IterableJSONCodec : JSONCodec<Iterable<*>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>): Iterable<*> =
		arrayListOf<Any?>().also { list ->
			decoder.readListByElement { list += readValue() }
		}


	override fun encode(value: Iterable<*>, encoder: JSONEncoder<out JSONCoderContext>) {
		encoder.writeList {
			for (element in value) {
				writeEncodableOrNull(element)
			}
		}
	}


	override val valueClass = Iterable::class.java
}
