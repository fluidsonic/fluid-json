package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractJSONEncoderCodec<Value : Any, in Context : JSONCoderContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList()
) : JSONEncoderCodec<Value, Context> {

	@Suppress("LeakingThis")
	final override val encodableClass = JSONCodableType.of<Value>(this::class.java.genericSuperclass as ParameterizedType).rawClass


	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? {
		for (provider in additionalProviders) {
			val codec = provider.decoderCodecForType(decodableType)
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
