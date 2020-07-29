package io.fluidsonic.json

import java.io.*


public interface JsonEncoder<out Context : JsonCodingContext> : JsonWriter {

	public val context: Context


	public companion object {

		public fun builder(): BuilderForCodecs<JsonCodingContext> =
			BuilderForCodecsImpl(context = JsonCodingContext.empty)


		public fun <Context : JsonCodingContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		public interface Builder<out Context : JsonCodingContext> {

			public fun build(): JsonEncoder<Context>
		}


		public interface BuilderForCodecs<out Context : JsonCodingContext> {

			public fun codecs(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): BuilderForDestination<Context> =
				codecs(providers = providers.toList(), base = base)


			public fun codecs(
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


		public interface BuilderForDestination<out Context : JsonCodingContext> {

			public fun destination(destination: JsonWriter): Builder<Context>


			public fun destination(destination: Writer): Builder<Context> =
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


public fun JsonEncoder<*>.serializationError(message: String): Nothing =
	throw JsonException.Serialization(
		message = message,
		path = path
	)
