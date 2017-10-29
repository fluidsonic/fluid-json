package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONCodecProvider<in Context : JSONCoderContext> {

	fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>?
	fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>?


	companion object {

		val default by lazy {
			of(
				AnyJSONDecoderCodec,
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
				ListJSONDecoderCodec,
				LongArrayJSONCodec,
				LongJSONCodec,
				MapJSONCodec,
				SequenceJSONCodec,
				ShortArrayJSONCodec,
				ShortJSONCodec,
				StringJSONCodec,
				IterableJSONEncoderCodec, // after subclasses
				NumberJSONCodec, // after subclasses
				appendDefault = false
			)
		}


		val nonRecursive by lazy {
			of(
				ArrayJSONCodec.nonRecursive,
				ListJSONDecoderCodec.nonRecursive,
				MapJSONCodec.nonRecursive,
				SequenceJSONCodec.nonRecursive,
				IterableJSONEncoderCodec.nonRecursive // after subclasses
			)
		}


		fun <Context : JSONCoderContext> of(
			vararg providers: JSONCodecProvider<Context>,
			appendDefault: Boolean = true
		) =
			of(providers.asIterable(), appendDefault = appendDefault)


		fun <Context : JSONCoderContext> of(
			providers: Iterable<JSONCodecProvider<Context>>,
			appendDefault: Boolean = true
		): JSONCodecProvider<Context> =
			if (appendDefault)
				StandardCodecProvider(providers = providers + default)
			else
				StandardCodecProvider(providers = providers)
	}
}


inline fun <reified Value : Any, Context : JSONCoderContext> JSONCodecProvider<Context>.decoderCodecForType() =
	decoderCodecForType(jsonCodableType<Value>())


inline fun <reified Value : Any, Context : JSONCoderContext> JSONCodecProvider<Context>.encoderCodecForClass() =
	encoderCodecForClass(Value::class)
