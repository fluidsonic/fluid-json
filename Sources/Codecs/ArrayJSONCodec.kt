package com.github.fluidsonic.fluid.json


object ArrayJSONCodec : JSONEncoderCodec<Array<out Any?>, JSONCoderContext> {

	override fun encode(value: Array<out Any?>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(Array<out Any?>::class.java)
}
