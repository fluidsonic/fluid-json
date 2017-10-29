package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


internal class StandardCodecProvider<in Context : JSONCoderContext>(
	providers: Iterable<JSONCodecProvider<Context>>
) : JSONCodecProvider<Context> {

	private val providers = providers.toSet()

	private val decoderCodecByType = mutableMapOf<JSONCodableType<*>, JSONDecoderCodec<*, Context>>()
	private val encoderCodecByClass = mutableMapOf<KClass<*>, JSONEncoderCodec<*, Context>>()


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? {
		val codecs = decoderCodecByType

		var decoder = synchronized(codecs) { codecs[decodableType] }
		if (decoder != null) {
			return decoder as JSONDecoderCodec<out Value, Context>?
		}

		for (provider in providers) {
			decoder = provider.decoderCodecForType(decodableType)
			if (decoder != null) {
				synchronized(codecs) {
					codecs[decodableType] = decoder!! // https://youtrack.jetbrains.com/issue/KT-7186
				}

				return decoder
			}
		}

		return null
	}


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? {
		val codecs = encoderCodecByClass

		var encoder = synchronized(codecs) { codecs[encodableClass] }
		if (encoder != null) {
			return encoder as JSONEncoderCodec<Value, Context>?
		}

		for (provider in providers) {
			encoder = provider.encoderCodecForClass(encodableClass)
			if (encoder != null) {
				synchronized(codecs) {
					codecs[encodableClass] = encoder!! // https://youtrack.jetbrains.com/issue/KT-7186
				}

				return encoder
			}
		}

		return null
	}
}
