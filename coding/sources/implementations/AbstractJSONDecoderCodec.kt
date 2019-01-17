package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractJSONDecoderCodec<Value : Any, in Context : JSONCodingContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList()
) : JSONDecoderCodec<Value, Context> {

	@Suppress("LeakingThis")
	final override val decodableType = JSONCodingType.fromGenericSupertype<Value>(this::class.java.genericSuperclass as ParameterizedType)


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodingType<in Value>): JSONDecoderCodec<out Value, Context>? {
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
		for (provider in additionalProviders) {
			val codec = provider.encoderCodecForClass(encodableClass)
			if (codec != null) {
				return codec
			}
		}

		return null
	}
}
