package com.github.fluidsonic.fluid.json

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


internal class FixedCodecProvider<in Context : JSONCodingContext>(
	providers: Iterable<JSONCodecProvider<Context>>
) : JSONCodecProvider<Context> {

	private val providers = providers.toSet().toTypedArray()

	private val decoderCodecByType = ConcurrentHashMap<JSONCodingType<*>, JSONDecoderCodec<*, Context>>()
	private val encoderCodecByClass = ConcurrentHashMap<KClass<*>, JSONEncoderCodec<*, Context>>()


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? {
		return decoderCodecByType.getOrPut(decodableType) {
			for (provider in providers) {
				val decoder = provider.decoderCodecForType(decodableType)
				if (decoder != null) {
					return@getOrPut decoder
				}
			}

			return null
		} as JSONDecoderCodec<ActualValue, Context>
	}


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? {
		return encoderCodecByClass.getOrPut(encodableClass) {
			for (provider in providers) {
				val encoder = provider.encoderCodecForClass(encodableClass)
				if (encoder != null) {
					return@getOrPut encoder
				}
			}

			return null
		} as JSONEncoderCodec<ActualValue, Context>
	}
}


@Suppress("FunctionName")
fun <Context : JSONCodingContext> JSONCodecProvider(
	vararg providers: JSONCodecProvider<Context>
) =
	JSONCodecProvider(providers.asIterable())


@Suppress("FunctionName")
fun <Context : JSONCodingContext> JSONCodecProvider(
	providers: Iterable<JSONCodecProvider<Context>>
): JSONCodecProvider<Context> =
	FixedCodecProvider(providers = providers)
