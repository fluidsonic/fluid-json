package com.github.fluidsonic.fluid.json


object FloatJSONCodec : JSONCodec<Float, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readFloat()


	override fun encode(value: Float, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeFloat(value)


	override val decodableClass = Float::class.java
}
