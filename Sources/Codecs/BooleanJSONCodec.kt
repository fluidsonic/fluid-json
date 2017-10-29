package com.github.fluidsonic.fluid.json


object BooleanJSONCodec : AbstractJSONCodec<Boolean, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Boolean>, decoder: JSONDecoder<out JSONCoderContext>) =
		decoder.readBoolean()


	override fun encode(value: Boolean, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeBoolean(value)
}
