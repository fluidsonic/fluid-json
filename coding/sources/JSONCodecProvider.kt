package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONCodecProvider<in Context : JSONCodingContext> {

	fun <Value : Any> decoderCodecForType(decodableType: JSONCodingType<in Value>): JSONDecoderCodec<out Value, Context>?
	fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>?


	companion object {

		val basic = of(DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)
		val extended = of(DefaultJSONCodecs.extended + DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)


		fun <Context : JSONCodingContext> of(
			vararg providers: JSONCodecProvider<Context>,
			base: JSONCodecProvider<JSONCodingContext>? = extended
		) =
			of(providers.asIterable(), base = base)


		fun <Context : JSONCodingContext> of(
			providers: Iterable<JSONCodecProvider<Context>>,
			base: JSONCodecProvider<JSONCodingContext>? = extended
		): JSONCodecProvider<Context> =
			StandardCodecProvider(providers = base?.let { providers + it } ?: providers)
	}
}


inline fun <reified Value : Any, Context : JSONCodingContext> JSONCodecProvider<Context>.decoderCodecForType() =
	decoderCodecForType(jsonCodingType<Value>())


inline fun <reified Value : Any, Context : JSONCodingContext> JSONCodecProvider<Context>.encoderCodecForClass() =
	encoderCodecForClass(Value::class)
