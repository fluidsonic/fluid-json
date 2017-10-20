package com.github.fluidsonic.fluid.json


object FloatJSONCodec : JSONCodec<Float, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readFloat()


	override fun encode(value: Float, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeFloat(value)


	override val valueClass = Float::class.java
}
