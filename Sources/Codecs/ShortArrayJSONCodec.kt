package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : JSONEncoderCodec<ShortArray, JSONCoderContext> {

	override fun encode(value: ShortArray, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeList(value)


	override val valueClass = ShortArray::class.java
}
