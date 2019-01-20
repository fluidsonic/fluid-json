package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONEncoderCodec<Value : Any, in Context : JSONCodingContext> : JSONCodecProvider<Context> {

	val encodableClass: KClass<Value>


	fun JSONEncoder<Context>.encode(value: Value)


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodingType<in Value>): JSONDecoderCodec<out Value, Context>? =
		null


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? =
		if (encodableClass.isAssignableOrBoxableTo(this.encodableClass))
			this as JSONEncoderCodec<in Value, Context>
		else
			null


	companion object
}
