package io.fluidsonic.json

import java.io.*


/**
 * A [JsonParser] that uses codecs to decode typed values from JSON.
 */
public interface JsonCodingParser<out Context : JsonCodingContext> : JsonParser {

	public fun createDecoder(source: JsonReader): JsonDecoder<Context>


	override fun parseList(source: JsonReader, withTermination: Boolean): List<*> =
		parseValueOfType(source, withTermination = withTermination)


	override fun parseMap(source: JsonReader, withTermination: Boolean): Map<String, *> =
		parseValueOfType(source, withTermination = withTermination)


	public fun <Value : Any> parseValueOfType(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean = true): Value =
		createDecoder(source).withTermination(withTermination) { readValueOfType(valueType) }


	public fun <Value : Any> parseValueOfTypeOrNull(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean = true): Value? =
		createDecoder(source).withTermination(withTermination) { readValueOfTypeOrNull(valueType) }


	override fun parseValueOrNull(source: JsonReader, withTermination: Boolean): Any? =
		parseValueOfTypeOrNull(source, withTermination = withTermination)


	public companion object {

		public fun builder(): BuilderForDecoding<JsonCodingContext> =
			BuilderForDecodingImpl(context = JsonCodingContext.empty)


		public fun <Context : JsonCodingContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		public val default: JsonCodingParser<JsonCodingContext> = builder()
			.decodingWith()
			.build()


		public val nonRecursive: JsonCodingParser<JsonCodingContext> = builder()
			.decodingWith(DefaultJsonCodecs.nonRecursive)
			.build()


		public interface Builder<out Context : JsonCodingContext> {

			public fun build(): JsonCodingParser<Context>
		}


		public interface BuilderForDecoding<Context : JsonCodingContext> {

			public fun decodingWith(factory: (source: JsonReader, context: Context) -> JsonDecoder<Context>): Builder<Context>


			public fun decodingWith(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): Builder<Context> =
				decodingWith(providers = providers.toList(), base = base)


			public fun decodingWith(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): Builder<Context> =
				decodingWith { source, context ->
					JsonDecoder.builder(context)
						.codecs(JsonCodecProvider(providers), base = base)
						.source(source)
						.build()
				}
		}


		private class BuilderForDecodingImpl<Context : JsonCodingContext>(
			private val context: Context
		) : BuilderForDecoding<Context> {

			override fun decodingWith(factory: (source: JsonReader, context: Context) -> JsonDecoder<Context>) =
				BuilderImpl(
					context = context,
					decoderFactory = factory
				)
		}


		private class BuilderImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val decoderFactory: (source: JsonReader, context: Context) -> JsonDecoder<Context>
		) : Builder<Context> {

			override fun build() =
				StandardCodingParser(
					context = context,
					decoderFactory = decoderFactory
				)
		}
	}
}


/** Parses a non-null JSON value from a [JsonReader] source using codecs. */
public fun JsonCodingParser<*>.parseValue(source: JsonReader, withTermination: Boolean = true): Any =
	parseValueOrNull(source, withTermination = withTermination)
		?: throw JsonException.Schema(
			message = "Unexpected null value at top-level",
			offset = source.offset,
			path = source.path
		)


/** Parses a non-null JSON value from a [Reader] source using codecs. */
public fun JsonCodingParser<*>.parseValue(source: Reader, withTermination: Boolean = true): Any =
	parseValue(JsonReader.build(source), withTermination = withTermination)


/** Parses a non-null JSON value from a [String] source using codecs. */
public fun JsonCodingParser<*>.parseValue(source: String): Any =
	parseValue(JsonReader.build(source))


/** Parses a JSON value from a [JsonReader] and decodes it as the reified type [Value]. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: JsonReader, withTermination: Boolean = true): Value =
	parseValueOfType(source, valueType = jsonCodingType(), withTermination = withTermination)


/** Parses a JSON value from a [Reader] and decodes it as the reified type [Value]. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: Reader, withTermination: Boolean = true): Value =
	parseValueOfType(JsonReader.build(source), withTermination = withTermination)


/** Parses a JSON value from a [String] and decodes it as the reified type [Value]. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: String): Value =
	parseValueOfType(JsonReader.build(source))


/** Parses a JSON value from a [Reader] and decodes it as [valueType]. */
public fun <Value : Any> JsonCodingParser<*>.parseValueOfType(source: Reader, valueType: JsonCodingType<Value>, withTermination: Boolean = true): Value =
	parseValueOfType(JsonReader.build(source), valueType = valueType, withTermination = withTermination)


/** Parses a JSON value from a [String] and decodes it as [valueType]. */
public fun <Value : Any> JsonCodingParser<*>.parseValueOfType(source: String, valueType: JsonCodingType<Value>): Value =
	parseValueOfType(JsonReader.build(source), valueType = valueType)


/** Parses a JSON value from a [JsonReader] as the reified type [Value], or `null` if the value is a JSON null. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: JsonReader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodingType(), withTermination = withTermination)


/** Parses a JSON value from a [Reader] as the reified type [Value], or `null` if the value is a JSON null. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: Reader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), withTermination = withTermination)


/** Parses a JSON value from a [String] as the reified type [Value], or `null` if the value is a JSON null. */
public inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source))


/** Parses a JSON value from a [Reader] as [valueType], or `null` if the value is a JSON null. */
public fun <Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: Reader, valueType: JsonCodingType<Value>, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), valueType = valueType, withTermination = withTermination)


/** Parses a JSON value from a [String] as [valueType], or `null` if the value is a JSON null. */
public fun <Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: String, valueType: JsonCodingType<Value>): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), valueType = valueType)


/** Parses a nullable JSON value from a [Reader] source using codecs. */
public fun JsonCodingParser<*>.parseValueOrNull(source: Reader, withTermination: Boolean = true): Any? =
	parseValueOrNull(JsonReader.build(source), withTermination = withTermination)


/** Parses a nullable JSON value from a [String] source using codecs. */
public fun JsonCodingParser<*>.parseValueOrNull(source: String): Any? =
	parseValueOrNull(JsonReader.build(source))
