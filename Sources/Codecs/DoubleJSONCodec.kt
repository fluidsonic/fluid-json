package com.github.fluidsonic.fluid.json


object DoubleJSONCodec : JSONCodec<Double, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readDouble()


	override fun encode(value: Double, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeDouble(value)


	override val valueClass = Double::class.java
}
