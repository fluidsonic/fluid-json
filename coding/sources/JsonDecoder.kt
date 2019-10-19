package io.fluidsonic.json

import java.io.*


interface JsonDecoder<out Context : JsonCodingContext> : JsonReader {

	val context: Context


	override fun readValue() =
		readValueOfType<Any>()


	fun <Value : Any> readValueOfType(valueType: JsonCodingType<Value>): Value


	companion object {

		fun builder(): BuilderForCodecs<JsonCodingContext> =
			BuilderForCodecsImpl(context = JsonCodingContext.empty)


		fun <Context : JsonCodingContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		interface BuilderForCodecs<out Context : JsonCodingContext> {

			fun codecs(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			) =
				codecs(providers = providers.toList(), base = base)


			fun codecs(
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


		interface BuilderForSource<out Context : JsonCodingContext> {

			fun source(source: JsonReader): Builder<Context>


			fun source(source: Reader) =
				source(JsonReader.build(source))


			fun source(source: String) =
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


		interface Builder<out Context : JsonCodingContext> {

			fun build(): JsonDecoder<Context>
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


fun JsonDecoder<*>.invalidPropertyError(property: String, details: String): Nothing =
	schemaError("Invalid value for property '$property': $details")


fun JsonDecoder<*>.invalidValueError(details: String): Nothing =
	schemaError("Invalid value: $details")


fun JsonDecoder<*>.missingPropertyError(property: String): Nothing =
	schemaError("Missing value for property '$property'")


fun JsonDecoder<*>.parsingError(message: String): Nothing =
	throw JsonException.Parsing(
		message = message,
		offset = offset,
		path = path
	)


fun JsonDecoder<*>.readValueOrNull() =
	if (nextToken != JsonToken.nullValue) readValue() else readNull()


inline fun <reified Value : Any> JsonDecoder<*>.readValueOfType() =
	readValueOfType(jsonCodingType<Value>())


inline fun <reified Value : Any> JsonDecoder<*>.readValueOfTypeOrNull() =
	readValueOfTypeOrNull(jsonCodingType<Value>())


fun <Value : Any> JsonDecoder<*>.readValueOfTypeOrNull(valueType: JsonCodingType<Value>) =
	readOrNull { readValueOfType(valueType) }


fun JsonDecoder<*>.schemaError(message: String): Nothing =
	throw JsonException.Schema(
		message = message,
		offset = offset,
		path = path
	)
