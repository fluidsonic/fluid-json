package io.fluidsonic.json

import java.io.*


/**
 * A [JsonReader] that can decode values using registered codecs and provides access to a [JsonCodingContext].
 */
public interface JsonDecoder<out Context : JsonCodingContext> : JsonReader {

	public val context: Context


	override fun readValue(): Any =
		readValueOfType()


	public fun <Value : Any> readValueOfType(valueType: JsonCodingType<Value>): Value


	public companion object {

		public fun builder(): BuilderForCodecs<JsonCodingContext> =
			BuilderForCodecsImpl(context = JsonCodingContext.empty)


		public fun <Context : JsonCodingContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		public interface Builder<out Context : JsonCodingContext> {

			public fun build(): JsonDecoder<Context>
		}


		public interface BuilderForCodecs<out Context : JsonCodingContext> {

			public fun codecs(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): BuilderForSource<Context> =
				codecs(providers = providers.toList(), base = base)


			public fun codecs(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): BuilderForSource<Context>
		}


		private class BuilderForCodecsImpl<out Context : JsonCodingContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>?
			) =
				BuilderForSourceImpl(
					context = context,
					codecProvider = JsonCodecProvider(base?.let { providers + it } ?: providers)
				)
		}


		public interface BuilderForSource<out Context : JsonCodingContext> {

			public fun source(source: JsonReader): Builder<Context>


			public fun source(source: Reader): Builder<Context> =
				source(JsonReader.build(source))


			public fun source(source: String): Builder<Context> =
				source(StringReader(source))
		}


		private class BuilderForSourceImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val codecProvider: JsonCodecProvider<Context>
		) : BuilderForSource<Context> {

			override fun source(source: JsonReader) =
				BuilderImpl(
					context = context,
					codecProvider = codecProvider,
					source = source
				)
		}


		private class BuilderImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val codecProvider: JsonCodecProvider<Context>,
			private val source: JsonReader
		) : Builder<Context> {

			override fun build() =
				StandardDecoder(
					context = context,
					codecProvider = codecProvider,
					source = source
				)
		}
	}
}


/** Throws a [JsonException.Schema] indicating an invalid value for the given [property]. */
public fun JsonDecoder<*>.invalidPropertyError(property: String, details: String): Nothing =
	schemaError("Invalid value for property '$property': $details")


/** Throws a [JsonException.Schema] indicating an invalid value. */
public fun JsonDecoder<*>.invalidValueError(details: String): Nothing =
	schemaError("Invalid value: $details")


/** Throws a [JsonException.Schema] indicating a missing required [property]. */
public fun JsonDecoder<*>.missingPropertyError(property: String): Nothing =
	schemaError("Missing value for property '$property'")


/** Throws a [JsonException.Parsing] with the given [message]. */
public fun JsonDecoder<*>.parsingError(message: String): Nothing =
	throw JsonException.Parsing(
		message = message,
		offset = offset,
		path = path
	)


/** Reads any JSON value using codecs, or `null` if the next token is a JSON null. */
public fun JsonDecoder<*>.readValueOrNull(): Any? =
	if (nextToken != JsonToken.nullValue) readValue() else readNull()


/** Reads a JSON value and decodes it as the reified type [Value] using registered codecs. */
public inline fun <reified Value : Any> JsonDecoder<*>.readValueOfType(): Value =
	readValueOfType(jsonCodingType())


/** Reads a JSON value and decodes it as the reified type [Value], or `null` if the next token is a JSON null. */
public inline fun <reified Value : Any> JsonDecoder<*>.readValueOfTypeOrNull(): Value? =
	readValueOfTypeOrNull(jsonCodingType())


/** Reads a JSON value and decodes it as [valueType], or `null` if the next token is a JSON null. */
public fun <Value : Any> JsonDecoder<*>.readValueOfTypeOrNull(valueType: JsonCodingType<Value>): Value? =
	readOrNull { readValueOfType(valueType) }


/** Throws a [JsonException.Schema] with the given [message]. */
public fun JsonDecoder<*>.schemaError(message: String): Nothing =
	throw JsonException.Schema(
		message = message,
		offset = offset,
		path = path
	)
