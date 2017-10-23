package com.github.fluidsonic.fluid.json


object ShortArrayJSONCodec : JSONEncoderCodec<ShortArray, JSONCoderContext> {

	override fun encode(value: ShortArray, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	override val encodableClasses = setOf(ShortArray::class)
}
