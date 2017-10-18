package com.github.fluidsonic.fluid.json


internal interface JSONCodecResolver<in Context : JSONCoderContext>
	: JSONCodecProvider<Context> {

	fun <Value : Any> decoderCodecForClass(`class`: Class<in Value>): JSONDecoderCodec<Value, Context>?

	fun <Value : Any> encoderCodecForClass(`class`: Class<out Value>): JSONEncoderCodec<Value, Context>?


	companion object {

		private fun <Context : JSONCoderContext> collectDecoderCodecs(
			codecs: List<JSONDecoderCodec<*, Context>>,
			into: MutableSet<JSONDecoderCodec<*, Context>>
		) {
			codecs
				.filter { into.add(it) }
				.forEach { collectDecoderCodecs(it.decoderCodecs, into) }
		}


		private fun <Context : JSONCoderContext> collectEncoderCodecs(
			codecs: List<JSONEncoderCodec<*, Context>>,
			into: MutableSet<JSONEncoderCodec<*, Context>>
		) {
			codecs
				.filter { into.add(it) }
				.forEach { collectEncoderCodecs(it.encoderCodecs, into) }
		}


		fun <Context : JSONCoderContext> of(vararg providers: JSONCodecProvider<Context>): JSONCodecResolver<Context> =
			of(providers.asIterable())


		fun <Context : JSONCoderContext> of(providers: Iterable<JSONCodecProvider<Context>>): JSONCodecResolver<Context> =
			SimpleCodecResolver(
				decoderCodecs = mutableSetOf<JSONDecoderCodec<*, Context>>()
					.apply { collectDecoderCodecs(providers.flatMap { it.decoderCodecs }, into = this) }
					.toList(),
				encoderCodecs = mutableSetOf<JSONEncoderCodec<*, Context>>()
					.apply { collectEncoderCodecs(providers.flatMap { it.encoderCodecs }, into = this) }
					.toList()
			)
	}
}
