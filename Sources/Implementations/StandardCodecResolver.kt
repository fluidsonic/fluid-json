package com.github.fluidsonic.fluid.json


internal class StandardCodecResolver<in Context : JSONCoderContext>(
	decoderCodecs: List<JSONDecoderCodec<*, Context>>,
	encoderCodecs: List<JSONEncoderCodec<*, Context>>
) : JSONCodecResolver<Context> {

	private val resolvedDecoderCodecs = mutableMapOf<Class<*>, JSONDecoderCodec<*, Context>?>()
	private val resolvedEncoderCodecs = mutableMapOf<Class<*>, JSONEncoderCodec<*, Context>?>()

	override val decoderCodecs = decoderCodecs.toList() // defensive copy
	override val encoderCodecs = encoderCodecs.toList() // defensive copy


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> decoderCodecForClass(`class`: Class<in Value>): JSONDecoderCodec<Value, Context>? {
		val codecs = resolvedDecoderCodecs

		var decoder = synchronized(codecs) { codecs[`class`] }
		if (decoder == null) {
			decoder = decoderCodecs.firstOrNull { `class`.isAssignableFrom(it.valueClass) }
			if (decoder != null) {
				synchronized(codecs) {
					codecs[`class`] = decoder
				}
			}
		}

		return decoder as JSONDecoderCodec<Value, Context>?
	}


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> encoderCodecForClass(`class`: Class<out Value>): JSONEncoderCodec<Value, Context>? {
		val codecs = resolvedEncoderCodecs

		var encoder = synchronized(codecs) { codecs[`class`] }
		if (encoder == null) {
			encoder = encoderCodecs.firstOrNull { it.valueClass.isAssignableFrom(`class`) }
			if (encoder != null) {
				synchronized(codecs) {
					codecs[`class`] = encoder
				}
			}
		}

		return encoder as JSONEncoderCodec<Value, Context>?
	}
}
