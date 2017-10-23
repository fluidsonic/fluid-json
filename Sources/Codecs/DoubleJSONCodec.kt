package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : JSONCodec<Double, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readDouble()


	override fun encode(value: Double, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeDouble(value)


	override val decodableClass = Double::class
}
