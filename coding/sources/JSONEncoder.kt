package com.github.fluidsonic.fluid.json

import java.io.*


interface JSONEncoder<out Context : JSONCodingContext> : JSONWriter {

	val context: Context


	companion object {

		fun builder(): BuilderForCodecs<JSONCodingContext> =
			BuilderForCodecsImpl(context = JSONCodingContext.empty)


		fun <Context : JSONCodingContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		interface BuilderForCodecs<out Context : JSONCodingContext> {

			fun codecs(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				codecs(providers = providers.toList(), base = base)


			fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			): BuilderForDestination<Context>
		}


		private class BuilderForCodecsImpl<out Context : JSONCodingContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCodingContext>?
			) =
				BuilderForDestinationImpl(
					context = context,
					codecProvider = JSONCodecProvider(base?.let { providers + it } ?: providers)
				)
		}


		interface BuilderForDestination<out Context : JSONCodingContext> {

			fun destination(destination: JSONWriter): Builder<Context>


			fun destination(destination: Writer) =
				destination(JSONWriter.build(destination))
		}


		private class BuilderForDestinationImpl<out Context : JSONCodingContext>(
			private val context: Context,
			private val codecProvider: JSONCodecProvider<Context>
		) : BuilderForDestination<Context> {

			override fun destination(destination: JSONWriter) =
				BuilderImpl(
					context = context,
					codecProvider = codecProvider,
					destination = destination
				)
		}


		interface Builder<out Context : JSONCodingContext> {

			fun build(): JSONEncoder<Context>
		}


		private class BuilderImpl<out Context : JSONCodingContext>(
			private val context: Context,
			private val codecProvider: JSONCodecProvider<Context>,
			private val destination: JSONWriter
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


fun JSONEncoder<*>.serializationError(message: String): Nothing =
	throw JSONException.Serialization(
		message = message,
		path = path
	)
