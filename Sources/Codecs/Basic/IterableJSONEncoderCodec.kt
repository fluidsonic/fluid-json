package com.github.fluidsonic.fluid.json


object IterableJSONEncoderCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCoderContext>() {

	override fun encode(value: Iterable<*>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Iterable<*>>()
}
