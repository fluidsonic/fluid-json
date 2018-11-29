package com.github.fluidsonic.fluid.json


object ArrayJSONCodec : AbstractJSONEncoderCodec<Array<out Any?>, JSONCodingContext>() {

	override fun encode(value: Array<out Any?>, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Array<out Any?>>()
}
