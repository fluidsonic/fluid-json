package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractJSONEncoderCodec<Value : Any, in Context : JSONCodingContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList(),
	encodableClass: KClass<Value>? = null
) : JSONEncoderCodec<Value, Context> {

	@Suppress("UNCHECKED_CAST")
	final override val encodableClass = encodableClass
		?: JSONCodingType.of((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]).rawClass as KClass<Value>


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? {
		for (provider in additionalProviders) {
			val codec = provider.decoderCodecForType(decodableType)
			if (codec != null) {
				return codec
			}
		}

		return null
	}


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? {
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
