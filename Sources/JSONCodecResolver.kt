package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONCodecResolver<in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	fun <Value : Any> decoderCodecForClass(`class`: KClass<out Value>): JSONDecoderCodec<Value, Context>?
	fun <Value : Any> encoderCodecForClass(`class`: KClass<out Value>): JSONEncoderCodec<in Value, Context>?


	companion object {

		val default by lazy {
			JSONCodecResolver.of(
				ArrayJSONCodec,
				BooleanArrayJSONCodec,
				BooleanJSONCodec,
				ByteArrayJSONCodec,
				ByteJSONCodec,
				DoubleArrayJSONCodec,
				DoubleJSONCodec,
				FloatArrayJSONCodec,
				FloatJSONCodec,
				IntArrayJSONCodec,
				IntJSONCodec,
				LongArrayJSONCodec,
				LongJSONCodec,
				MapJSONCodec,
				SequenceJSONCodec,
				ShortArrayJSONCodec,
				ShortJSONCodec,
				StringJSONCodec,
				IterableJSONCodec, // after subclasses
				NumberJSONCodec, // after subclasses
				AnyJSONCodec,
				appendDefaultCodecs = false
			)
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
			if (appendDefaultCodecs)
				StandardCodecResolver.of(providers = providers + default)
			else
				StandardCodecResolver.of(providers = providers)
	}
}
