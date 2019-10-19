package io.fluidsonic.json

import kotlin.reflect.*


interface JsonCodecProvider<in Context : JsonCodingContext> {

	fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>?
	fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>?


	companion object {

		@Deprecated(message = "replaced by JsonCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
		fun <Context : JsonCodingContext> of(
			vararg providers: JsonCodecProvider<Context>,
			base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
		) =
			of(providers.asIterable(), base = base)


		@Deprecated(message = "replaced by JsonCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DeprecatedCallableAddReplaceWith")
		fun <Context : JsonCodingContext> of(
			providers: Iterable<JsonCodecProvider<Context>>,
			base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
		): JsonCodecProvider<Context> =
			FixedCodecProvider(providers = base?.let { providers + it } ?: providers)

	}
}


private val basicProvider = JsonCodecProvider(DefaultJsonCodecs.basic + DefaultJsonCodecs.nonRecursive)
private val extendedProvider = JsonCodecProvider(DefaultJsonCodecs.extended + DefaultJsonCodecs.basic + DefaultJsonCodecs.nonRecursive)


val JsonCodecProvider.Companion.basic
	get() = basicProvider


inline fun <reified ActualValue : Any, Context : JsonCodingContext> JsonCodecProvider<Context>.decoderCodecForType() =
	decoderCodecForType(jsonCodingType<ActualValue>())


inline fun <reified ActualValue : Any, Context : JsonCodingContext> JsonCodecProvider<Context>.encoderCodecForClass() =
	encoderCodecForClass(ActualValue::class)


val JsonCodecProvider.Companion.extended
	get() = extendedProvider


fun <CodecProvider : JsonCodecProvider<*>> JsonCodecProvider.Companion.generated(interfaceClass: KClass<CodecProvider>): CodecProvider =
	error("Cannot find annotation-based codec provider. Either $interfaceClass hasn't been annotated with @Json.CodecProvider or kapt hasn't been run.")
