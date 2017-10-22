package com.github.fluidsonic.fluid.json


interface JSONEncoderCodec<Value : Any, in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	override val decoderCodecs: List<JSONDecoderCodec<*, Context>>
		get() = emptyList()

	override val encoderCodecs: List<JSONEncoderCodec<*, Context>>
		get() = listOf(this)

	val encodableClasses: Set<Class<out Value>>


	fun encode(value: Value, encoder: JSONEncoder<out Context>)


	companion object
}
