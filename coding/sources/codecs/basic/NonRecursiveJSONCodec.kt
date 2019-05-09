package com.github.fluidsonic.fluid.json

import kotlin.reflect.*


internal abstract class NonRecursiveJSONCodec<Value : Any> private constructor(
	private val encoder: NonRecursiveJSONEncoderCodec<Value>
) :
	NonRecursiveJSONDecoderCodec<Value>(),
	JSONCodec<Value, JSONCodingContext>,
	JSONEncoderCodec<Value, JSONCodingContext> by encoder {


	override val encodableClass
		get() = encoder.encodableClass


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>) =
		super<NonRecursiveJSONDecoderCodec>.decoderCodecForType(decodableType)


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>) =
		encoder.encoderCodecForClass(encodableClass)


	companion object {

		inline fun <reified Value : Any> create(): JSONCodec<Value, JSONCodingContext> =
			object : NonRecursiveJSONCodec<Value>(
				encoder = NonRecursiveJSONEncoderCodec(encodableClass = Value::class)
			) {}
	}
}
