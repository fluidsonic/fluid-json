package com.github.fluidsonic.fluid.json


interface JSONCodecResolver<in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	fun <Value : Any> decoderCodecForClass(`class`: Class<in Value>): JSONDecoderCodec<Value, Context>?
	fun <Value : Any> encoderCodecForClass(`class`: Class<out Value>): JSONEncoderCodec<Value, Context>?


	companion object {

		val default by lazy {
			JSONCodecResolver.of(
				// FIXME add missing codecs
				ArrayJSONCodec,
				BooleanArrayJSONCodec,
				ByteArrayJSONCodec,
				DoubleArrayJSONCodec,
				FloatArrayJSONCodec,
				IntArrayJSONCodec,
				LongArrayJSONCodec,
				MapJSONCodec,
				SequenceJSONCodec,
				ShortArrayJSONCodec,
				IterableJSONCodec, // after subclasses (Map)
				appendDefaultCodecs = false
			)
		}


		private fun <Context : JSONCoderContext> collectDecoderCodecs(
			codecs: List<JSONDecoderCodec<*, Context>>,
			into: MutableMap<Class<*>, JSONDecoderCodec<*, Context>>
		) {
			codecs
				.filter { into.putIfAbsent(it.valueClass, it) == null }
				.forEach { collectDecoderCodecs(it.decoderCodecs, into) }
		}


		private fun <Context : JSONCoderContext> collectEncoderCodecs(
			codecs: List<JSONEncoderCodec<*, Context>>,
			into: MutableMap<Class<*>, JSONEncoderCodec<*, Context>>
		) {
			codecs
				.filter { into.putIfAbsent(it.valueClass, it) == null }
				.forEach { collectEncoderCodecs(it.encoderCodecs, into) }
		}


		fun <Context : JSONCoderContext> of(
			vararg providers: JSONCodecProvider<Context>,
			appendDefaultCodecs: Boolean = true
		) =
			of(providers.asIterable(), appendDefaultCodecs = appendDefaultCodecs)


		fun <Context : JSONCoderContext> of(
			providers: Iterable<JSONCodecProvider<Context>>,
			appendDefaultCodecs: Boolean = true
		): JSONCodecResolver<Context> =
			StandardCodecResolver(
				decoderCodecs = mutableMapOf<Class<*>, JSONDecoderCodec<*, Context>>()
					.apply {
						collectDecoderCodecs(providers.flatMap { it.decoderCodecs }, into = this)

						if (appendDefaultCodecs) {
							collectDecoderCodecs(default.decoderCodecs, into = this)
						}
					}
					.values
					.toList(),
				encoderCodecs = mutableMapOf<Class<*>, JSONEncoderCodec<*, Context>>()
					.apply {
						collectEncoderCodecs(providers.flatMap { it.encoderCodecs }, into = this)

						if (appendDefaultCodecs) {
							collectEncoderCodecs(default.encoderCodecs, into = this)
						}
					}
					.values
					.toList()
			)
	}
}
