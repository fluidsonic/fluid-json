package com.github.fluidsonic.fluid.json


// FIXME arrays are a different beast - test & fix implementation in codec resolver!
object ArrayJSONCodec : JSONEncoderCodec<Array<Any?>, JSONCoderContext> {

	override fun encode(value: Array<Any?>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = Array<Any?>::class.java
}
