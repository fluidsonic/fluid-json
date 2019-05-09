package com.github.fluidsonic.fluid.json

import kotlin.reflect.*


interface JSONEncoderCodec<in Value : Any, in Context : JSONCodingContext> : JSONCodecProvider<Context> {

	val encodableClass: KClass<in Value>


	fun JSONEncoder<Context>.encode(value: Value)


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? =
		null


	@Suppress("UNCHECKED_CAST")
	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? =
		if (encodableClass.isAssignableOrBoxableTo(this.encodableClass))
			this as JSONEncoderCodec<ActualValue, Context>
		else
			null


	companion object
}
