package com.github.fluidsonic.fluid.json


object ArrayJSONCodec : AbstractJSONEncoderCodec<Array<out Any?>, JSONCodingContext>() {

	override fun JSONEncoder<JSONCodingContext>.encode(value: Array<out Any?>) =
		writeList(value)


	val nonRecursive = NonRecursiveJSONEncoderCodec.create<Array<out Any?>>()
}
