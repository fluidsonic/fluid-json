package io.fluidsonic.json

import kotlin.reflect.*


public interface JsonCodec<Value : Any, in Context : JsonCodingContext>
	: JsonDecoderCodec<Value, Context>, JsonEncoderCodec<Value, Context> {

	override val decodableType: JsonCodingType<Value>


	override val encodableClass: KClass<Value>
		get() = decodableType.rawClass


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? =
		super<JsonDecoderCodec>.decoderCodecForType(decodableType)


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? =
		super<JsonEncoderCodec>.encoderCodecForClass(encodableClass)


	public companion object
}
