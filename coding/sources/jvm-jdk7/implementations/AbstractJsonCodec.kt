package io.fluidsonic.json

import java.lang.reflect.*
import kotlin.reflect.*


public abstract class AbstractJsonCodec<Value : Any, in Context : JsonCodingContext>(
	private val additionalProviders: List<JsonCodecProvider<Context>> = emptyList(),
	decodableType: JsonCodingType<Value>? = null
) : JsonCodec<Value, Context> {

	@Suppress("UNCHECKED_CAST")
	final override val decodableType: JsonCodingType<Value> = decodableType
		?: JsonCodingType.of((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]) as JsonCodingType<Value>

	final override val encodableClass: KClass<Value> = this.decodableType.rawClass


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? {
		var codec = super.decoderCodecForType(decodableType)
		if (codec != null) {
			return codec
		}

		for (provider in additionalProviders) {
			codec = provider.decoderCodecForType(decodableType)
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
