package io.fluidsonic.json

import java.util.concurrent.*
import kotlin.reflect.*


internal class FixedCodecProvider<in Context : JsonCodingContext>(
	providers: Iterable<JsonCodecProvider<Context>>
) : JsonCodecProvider<Context> {

	private val providers = providers.toSet().toTypedArray()

	private val decoderCodecByType = ConcurrentHashMap<JsonCodingType<*>, JsonDecoderCodec<*, Context>>()
	private val encoderCodecByClass = ConcurrentHashMap<KClass<*>, JsonEncoderCodec<*, Context>>()


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? {
		return decoderCodecByType.getOrPut(decodableType) {
			for (provider in providers) {
				val decoder = provider.decoderCodecForType(decodableType)
				if (decoder != null) {
					return@getOrPut decoder
				}
			}

			return null
		} as JsonDecoderCodec<ActualValue, Context>
	}


	@Suppress("LoopToCallChain", "UNCHECKED_CAST")
	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? {
		return encoderCodecByClass.getOrPut(encodableClass) {
			for (provider in providers) {
				val encoder = provider.encoderCodecForClass(encodableClass)
				if (encoder != null) {
					return@getOrPut encoder
				}
			}

			return null
		} as JsonEncoderCodec<ActualValue, Context>
	}
}


@Suppress("FunctionName")
fun <Context : JsonCodingContext> JsonCodecProvider(
	vararg providers: JsonCodecProvider<Context>
) =
	JsonCodecProvider(providers.asIterable())


@Suppress("FunctionName")
fun <Context : JsonCodingContext> JsonCodecProvider(
	providers: Iterable<JsonCodecProvider<Context>>
): JsonCodecProvider<Context> =
	FixedCodecProvider(providers = providers)
