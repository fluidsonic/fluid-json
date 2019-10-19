package io.fluidsonic.json

import java.io.*


interface JsonCodingParser<out Context : JsonCodingContext> : JsonParser {

	fun createDecoder(source: JsonReader): JsonDecoder<Context>


	override fun parseList(source: JsonReader, withTermination: Boolean) =
		parseValueOfType<List<*>>(source, withTermination = withTermination)


	override fun parseMap(source: JsonReader, withTermination: Boolean) =
		parseValueOfType<Map<String, *>>(source, withTermination = withTermination)


	fun <Value : Any> parseValueOfType(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean = true) =
		createDecoder(source).withTermination(withTermination) { readValueOfType(valueType) }


	fun <Value : Any> parseValueOfTypeOrNull(source: JsonReader, valueType: JsonCodingType<Value>, withTermination: Boolean = true) =
		createDecoder(source).withTermination(withTermination) { readValueOfTypeOrNull(valueType) }


	override fun parseValueOrNull(source: JsonReader, withTermination: Boolean) =
		parseValueOfTypeOrNull<Any>(source, withTermination = withTermination)


	companion object {

		fun builder(): BuilderForDecoding<JsonCodingContext> =
			BuilderForDecodingImpl(context = JsonCodingContext.empty)


		fun <Context : JsonCodingContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		val default = builder()
			.decodingWith()
			.build()


		val nonRecursive = builder()
			.decodingWith(DefaultJsonCodecs.nonRecursive)
			.build()


		interface BuilderForDecoding<Context : JsonCodingContext> {

			fun decodingWith(factory: (source: JsonReader, context: Context) -> JsonDecoder<Context>): Builder<Context>


			fun decodingWith(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			) =
				decodingWith(providers = providers.toList(), base = base)


			fun decodingWith(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			) =
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


		interface Builder<out Context : JsonCodingContext> {

			fun build(): JsonCodingParser<Context>
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


fun JsonCodingParser<*>.parseValue(source: JsonReader, withTermination: Boolean = true) =
	parseValueOrNull(source, withTermination = withTermination)
		?: throw JsonException.Schema(
			message = "Unexpected null value at top-level",
			offset = source.offset,
			path = source.path
		)


fun JsonCodingParser<*>.parseValue(source: Reader, withTermination: Boolean = true) =
	parseValue(JsonReader.build(source), withTermination = withTermination)


fun JsonCodingParser<*>.parseValue(source: String) =
	parseValue(JsonReader.build(source))


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: JsonReader, withTermination: Boolean = true): Value =
	parseValueOfType(source, valueType = jsonCodingType(), withTermination = withTermination)


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: Reader, withTermination: Boolean = true): Value =
	parseValueOfType(JsonReader.build(source), withTermination = withTermination)


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfType(source: String): Value =
	parseValueOfType(JsonReader.build(source))


fun <Value : Any> JsonCodingParser<*>.parseValueOfType(source: Reader, valueType: JsonCodingType<Value>, withTermination: Boolean = true) =
	parseValueOfType(JsonReader.build(source), valueType = valueType, withTermination = withTermination)


fun <Value : Any> JsonCodingParser<*>.parseValueOfType(source: String, valueType: JsonCodingType<Value>): Value =
	parseValueOfType(JsonReader.build(source), valueType = valueType)


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: JsonReader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodingType(), withTermination = withTermination)


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: Reader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), withTermination = withTermination)


inline fun <reified Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source))


fun <Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: Reader, valueType: JsonCodingType<Value>, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), valueType = valueType, withTermination = withTermination)


fun <Value : Any> JsonCodingParser<*>.parseValueOfTypeOrNull(source: String, valueType: JsonCodingType<Value>): Value? =
	parseValueOfTypeOrNull(JsonReader.build(source), valueType = valueType)


fun JsonCodingParser<*>.parseValueOrNull(source: Reader, withTermination: Boolean = true) =
	parseValueOrNull(JsonReader.build(source), withTermination = withTermination)


fun JsonCodingParser<*>.parseValueOrNull(source: String) =
	parseValueOrNull(JsonReader.build(source))
