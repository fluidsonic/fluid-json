package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONCodecProvider<in Context : JSONCodingContext> {

	fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>?
	fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>?


	companion object {

		@Deprecated(message = "replaced by JSONCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
		fun <Context : JSONCodingContext> of(
			vararg providers: JSONCodecProvider<Context>,
			base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
		) =
			of(providers.asIterable(), base = base)


		@Deprecated(message = "replaced by JSONCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DeprecatedCallableAddReplaceWith")
		fun <Context : JSONCodingContext> of(
			providers: Iterable<JSONCodecProvider<Context>>,
			base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
		): JSONCodecProvider<Context> =
			FixedCodecProvider(providers = base?.let { providers + it } ?: providers)

	}
}


private val basicProvider = JSONCodecProvider(DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)
private val extendedProvider = JSONCodecProvider(DefaultJSONCodecs.extended + DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)


val JSONCodecProvider.Companion.basic
	get() = basicProvider


val JSONCodecProvider.Companion.extended
	get() = extendedProvider


inline fun <reified ActualValue : Any, Context : JSONCodingContext> JSONCodecProvider<Context>.decoderCodecForType() =
	decoderCodecForType(jsonCodingType<ActualValue>())


inline fun <reified ActualValue : Any, Context : JSONCodingContext> JSONCodecProvider<Context>.encoderCodecForClass() =
	encoderCodecForClass(ActualValue::class)
