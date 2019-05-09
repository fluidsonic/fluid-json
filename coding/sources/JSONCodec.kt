package com.github.fluidsonic.fluid.json

import kotlin.reflect.*


interface JSONCodec<Value : Any, in Context : JSONCodingContext>
	: JSONDecoderCodec<Value, Context>, JSONEncoderCodec<Value, Context> {

	override val decodableType: JSONCodingType<Value>


	override val encodableClass: KClass<Value>
		get() = decodableType.rawClass


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>) =
		super<JSONDecoderCodec>.decoderCodecForType(decodableType)


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>) =
		super<JSONEncoderCodec>.encoderCodecForClass(encodableClass)


	companion object
}
