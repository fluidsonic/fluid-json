package com.github.fluidsonic.fluid.json


object ArrayJSONCodec : AbstractJSONEncoderCodec<Array<out Any?>, JSONCoderContext>() {

	override fun encode(value: Array<out Any?>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Array<out Any?>>()
}
