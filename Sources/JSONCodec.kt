package com.github.fluidsonic.fluid.json


interface JSONCodec<Value : Any, in Context : JSONCoderContext>
	: JSONDecoderCodec<Value, Context>, JSONEncoderCodec<Value, Context> {

	val codecs: List<JSONCodec<*, Context>>
		get() = listOf(this)

	override val encodableClasses: Set<Class<out Value>>
		get() = setOf(decodableClass)

	override val decoderCodecs: List<JSONDecoderCodec<*, Context>>
		get() = codecs

	override val encoderCodecs: List<JSONEncoderCodec<*, Context>>
		get() = codecs


	companion object
}
