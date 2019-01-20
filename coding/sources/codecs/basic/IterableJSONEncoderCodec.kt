package com.github.fluidsonic.fluid.json


object IterableJSONEncoderCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: Iterable<*>) =
		writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Iterable<*>>()
}
