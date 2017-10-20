package com.github.fluidsonic.fluid.json


object BooleanJSONCodec : JSONCodec<Boolean, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readBoolean()


	override fun encode(value: Boolean, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeBoolean(value)


	override val valueClass = Boolean::class.java
}
