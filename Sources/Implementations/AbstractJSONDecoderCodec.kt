package com.github.fluidsonic.fluid.json

import java.lang.reflect.ParameterizedType


abstract class AbstractJSONDecoderCodec<Value : Any, in Context : JSONCoderContext>(
	private val additionalProviders: List<JSONCodecProvider<Context>> = emptyList()
) : JSONDecoderCodec<Value, Context> {

	@Suppress("LeakingThis")
	final override val decodableType = JSONCodableType.of<Value>(this::class.java.genericSuperclass as ParameterizedType)


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
}
