package com.github.fluidsonic.fluid.json


internal interface JSONDecoderCodec<out Value : Any, in Context : JSONCoderContext>
	: JSONCodecProvider<Context> {

	fun decode(decoder: JSONDecoder<Context>): Value


	override val decoderCodecs: List<JSONDecoderCodec<*, Context>>
		get() = listOf(this)


	override val encoderCodecs: List<JSONEncoderCodec<*, Context>>
		get() = emptyList()


	val valueClass: Class<out Value>
}
