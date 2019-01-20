package com.github.fluidsonic.fluid.json


object BooleanJSONCodec : AbstractJSONCodec<Boolean, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Boolean>) =
		readBoolean()


	override fun JSONEncoder<JSONCodingContext>.encode(value: Boolean) =
		writeBoolean(value)
}
