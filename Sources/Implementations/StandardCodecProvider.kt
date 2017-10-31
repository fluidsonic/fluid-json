package com.github.fluidsonic.fluid.json

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


internal class StandardCodecProvider<in Context : JSONCoderContext>(
	providers: Iterable<JSONCodecProvider<Context>>
) : JSONCodecProvider<Context> {

	private val providers = providers.toSet().toTypedArray()

	private val decoderCodecByType = ConcurrentHashMap<JSONCodableType<*>, JSONDecoderCodec<*, Context>>()
	private val encoderCodecByClass = ConcurrentHashMap<KClass<*>, JSONEncoderCodec<*, Context>>()


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? {
		return decoderCodecByType.getOrPut(decodableType) {
			for (provider in providers) {
				val decoder = provider.decoderCodecForType(decodableType)
				if (decoder != null) {
					return@getOrPut decoder
				}
			}

			return null
		} as JSONDecoderCodec<out Value, Context>
	}


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? {
		return encoderCodecByClass.getOrPut(encodableClass) {
			for (provider in providers) {
				val encoder = provider.encoderCodecForClass(encodableClass)
				if (encoder != null) {
					return@getOrPut encoder
				}
			}

			return null
		} as JSONEncoderCodec<in Value, Context>
	}
}
