package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


internal abstract class NonRecursiveJSONCodec<Value : Any> private constructor(
	private val encoder: NonRecursiveJSONEncoderCodec<Value>
) :
	NonRecursiveJSONDecoderCodec<Value>(),
	JSONCodec<Value, JSONCoderContext>,
	JSONEncoderCodec<Value, JSONCoderContext> by encoder {


	override val encodableClass
		get() = encoder.encodableClass


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>) =
		super<NonRecursiveJSONDecoderCodec>.decoderCodecForType(decodableType)


	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>) =
		encoder.encoderCodecForClass(encodableClass)


	companion object {

		inline fun <reified Value : Any> create(): JSONCodec<Value, JSONCoderContext> =
			object : NonRecursiveJSONCodec<Value>(
				encoder = NonRecursiveJSONEncoderCodec(encodableClass = Value::class)
			) {}
	}
}
