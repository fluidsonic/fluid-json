package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractJSONCodec<Value : Any, in Context : JSONCoderContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList()
) : JSONCodec<Value, Context> {

	@Suppress("LeakingThis")
	final override val decodableType = JSONCodableType.of<Value>(this::class.java.genericSuperclass as ParameterizedType)

	final override val encodableClass = decodableType.rawClass


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? {
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


	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? {
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
