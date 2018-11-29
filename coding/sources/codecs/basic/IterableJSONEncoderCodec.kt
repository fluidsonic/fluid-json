package com.github.fluidsonic.fluid.json


object IterableJSONEncoderCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>() {

	override fun encode(value: Iterable<*>, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Iterable<*>>()
}
