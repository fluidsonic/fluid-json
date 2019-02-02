package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractJSONDecoderCodec<Value : Any, in Context : JSONCodingContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList(),
	decodableType: JSONCodingType<Value>? = null
) : JSONDecoderCodec<Value, Context> {

	@Suppress("UNCHECKED_CAST")
	final override val decodableType = decodableType
		?: JSONCodingType.of((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]) as JSONCodingType<Value>


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? {
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


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? {
		for (provider in additionalProviders) {
			val codec = provider.encoderCodecForClass(encodableClass)
			if (codec != null) {
				return codec
			}
		}

		return null
	}
}
