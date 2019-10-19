package io.fluidsonic.json

import kotlin.reflect.*


internal abstract class NonRecursiveJsonCodec<Value : Any> private constructor(
	private val encoder: NonRecursiveJsonEncoderCodec<Value>
) :
	NonRecursiveJsonDecoderCodec<Value>(),
	JsonCodec<Value, JsonCodingContext>,
	JsonEncoderCodec<Value, JsonCodingContext> by encoder {


	override val encodableClass
		get() = encoder.encodableClass


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>) =
		super<NonRecursiveJsonDecoderCodec>.decoderCodecForType(decodableType)


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>) =
		encoder.encoderCodecForClass(encodableClass)


	companion object {

		inline fun <reified Value : Any> create(): JsonCodec<Value, JsonCodingContext> =
			object : NonRecursiveJsonCodec<Value>(
				encoder = NonRecursiveJsonEncoderCodec(encodableClass = Value::class)
			) {}
	}
}
