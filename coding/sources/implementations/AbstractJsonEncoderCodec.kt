package io.fluidsonic.json

import java.lang.reflect.*
import kotlin.reflect.*


abstract class AbstractJsonEncoderCodec<Value : Any, in Context : JsonCodingContext>(
	private val additionalProviders: List<JsonCodecProvider<Context>> = emptyList(),
	encodableClass: KClass<Value>? = null
) : JsonEncoderCodec<Value, Context> {

	@Suppress("UNCHECKED_CAST")
	final override val encodableClass = encodableClass
		?: JsonCodingType.of((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]).rawClass as KClass<Value>


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? {
		for (provider in additionalProviders) {
			val codec = provider.decoderCodecForType(decodableType)
			if (codec != null) {
				return codec
			}
		}

		return null
	}


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? {
		var codec = super.encoderCodecForClass(encodableClass)
		if (codec != null) {
			return codec
		}

		for (provider in additionalProviders) {
			codec = provider.encoderCodecForClass(encodableClass)
			if (codec != null) {
				return codec
			}
		}

		return null
	}
}
