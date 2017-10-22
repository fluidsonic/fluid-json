package com.github.fluidsonic.fluid.json


object BooleanJSONCodec : JSONCodec<Boolean, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readBoolean()


	override fun encode(value: Boolean, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeBoolean(value)


	override val decodableClass = Boolean::class.java
}
