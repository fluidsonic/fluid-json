package io.fluidsonic.json

import java.io.*


interface JsonEncoder<out Context : JsonCodingContext> : JsonWriter {

	val context: Context


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
			): BuilderForDestination<Context>
		}


		private class BuilderForCodecsImpl<out Context : JsonCodingContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>?
			) =
				BuilderForDestinationImpl(
					context = context,
					codecProvider = JsonCodecProvider(base?.let { providers + it } ?: providers)
				)
		}


		interface BuilderForDestination<out Context : JsonCodingContext> {

			fun destination(destination: JsonWriter): Builder<Context>


			fun destination(destination: Writer) =
				destination(JsonWriter.build(destination))
		}


		private class BuilderForDestinationImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val codecProvider: JsonCodecProvider<Context>
		) : BuilderForDestination<Context> {

			override fun destination(destination: JsonWriter) =
				BuilderImpl(
					context = context,
					codecProvider = codecProvider,
					destination = destination
				)
		}


		interface Builder<out Context : JsonCodingContext> {

			fun build(): JsonEncoder<Context>
		}


		private class BuilderImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val codecProvider: JsonCodecProvider<Context>,
			private val destination: JsonWriter
		) : Builder<Context> {

			override fun build() =
				StandardEncoder(
					context = context,
					codecProvider = codecProvider,
					destination = destination
				)
		}
	}
}


fun JsonEncoder<*>.serializationError(message: String): Nothing =
	throw JsonException.Serialization(
		message = message,
		path = path
	)
