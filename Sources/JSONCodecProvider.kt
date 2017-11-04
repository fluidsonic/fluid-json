package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONCodecProvider<in Context : JSONCoderContext> {

	fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>?
	fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>?


	companion object {

		val basic = of(DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)
		val extended = of(DefaultJSONCodecs.extended + DefaultJSONCodecs.basic + DefaultJSONCodecs.nonRecursive)


		fun <Context : JSONCoderContext> of(
			vararg providers: JSONCodecProvider<Context>,
			base: JSONCodecProvider<JSONCoderContext>? = extended
		) =
			of(providers.asIterable(), base = base)


		fun <Context : JSONCoderContext> of(
			providers: Iterable<JSONCodecProvider<Context>>,
			base: JSONCodecProvider<JSONCoderContext>? = extended
		): JSONCodecProvider<Context> =
			StandardCodecProvider(providers = base?.let { providers + it } ?: providers)
	}
}


inline fun <reified Value : Any, Context : JSONCoderContext> JSONCodecProvider<Context>.decoderCodecForType() =
	decoderCodecForType(jsonCodableType<Value>())


inline fun <reified Value : Any, Context : JSONCoderContext> JSONCodecProvider<Context>.encoderCodecForClass() =
	encoderCodecForClass(Value::class)
