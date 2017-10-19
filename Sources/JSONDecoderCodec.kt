package com.github.fluidsonic.fluid.json


interface JSONDecoderCodec<out Value : Any, in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	override val decoderCodecs: List<JSONDecoderCodec<*, Context>>
		get() = listOf(this)

	override val encoderCodecs: List<JSONEncoderCodec<*, Context>>
		get() = emptyList()

	val valueClass: Class<out Value>


	fun decode(decoder: JSONDecoder<Context>): Value


	companion object
}
