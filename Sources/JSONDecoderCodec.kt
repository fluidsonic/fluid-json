package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONDecoderCodec<out Value : Any, in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	override val decoderCodecs: List<JSONDecoderCodec<*, Context>>
		get() = listOf(this)

	override val encoderCodecs: List<JSONEncoderCodec<*, Context>>
		get() = emptyList()

	val decodableClass: KClass<out Value>


	fun decode(decoder: JSONDecoder<out Context>): Value


	companion object
}
