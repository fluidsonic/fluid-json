package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONEncoderCodec<Value : Any, in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	val encodableClass: KClass<Value>


	fun encode(value: Value, encoder: JSONEncoder<Context>)


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? =
		null


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? =
		if (encodableClass.isAssignableOrBoxableTo(this.encodableClass))
			this as JSONEncoderCodec<in Value, Context>
		else
			null


	companion object
}
