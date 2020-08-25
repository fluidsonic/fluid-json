package io.fluidsonic.json

import kotlin.reflect.*


public interface JsonCodecProvider<in Context : JsonCodingContext> {

	public fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>?
	public fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>?


	public companion object {

		@Deprecated(message = "replaced by JsonCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
		public fun <Context : JsonCodingContext> of(
			vararg providers: JsonCodecProvider<Context>,
			base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
		): JsonCodecProvider<Context> =
			of(providers.asIterable(), base = base)


		@Deprecated(message = "replaced by JsonCodecProvider(…), but be careful because that one no longer adds base providers automatically")
		@Suppress("DeprecatedCallableAddReplaceWith")
		public fun <Context : JsonCodingContext> of(
			providers: Iterable<JsonCodecProvider<Context>>,
			base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
		): JsonCodecProvider<Context> =
			FixedCodecProvider(providers = base?.let { providers + it } ?: providers)

	}
}


private val basicProvider = JsonCodecProvider(DefaultJsonCodecs.basic + DefaultJsonCodecs.nonRecursive)
private val extendedProvider = JsonCodecProvider(DefaultJsonCodecs.extended + DefaultJsonCodecs.basic + DefaultJsonCodecs.nonRecursive)


public val JsonCodecProvider.Companion.basic: JsonCodecProvider<JsonCodingContext>
	get() = basicProvider


public inline fun <reified ActualValue : Any, Context : JsonCodingContext> JsonCodecProvider<Context>.decoderCodecForType(): JsonDecoderCodec<ActualValue, Context>? =
	decoderCodecForType(jsonCodingType())


public inline fun <reified ActualValue : Any, Context : JsonCodingContext> JsonCodecProvider<Context>.encoderCodecForClass(): JsonEncoderCodec<ActualValue, Context>? =
	encoderCodecForClass(ActualValue::class)


public val JsonCodecProvider.Companion.extended: JsonCodecProvider<JsonCodingContext>
	get() = extendedProvider


public fun <CodecProvider : JsonCodecProvider<*>> JsonCodecProvider.Companion.generated(interfaceClass: KClass<CodecProvider>): CodecProvider =
	error("Cannot find annotation-based codec provider. Either $interfaceClass hasn't been annotated with @Json.CodecProvider or kapt hasn't been run.")
