package com.github.fluidsonic.fluid.json

import java.io.Writer


interface JSONEncoder<out Context : JSONCoderContext> : JSONWriter {

	val context: Context


	companion object {

		fun builder(): BuilderForCodecs<JSONCoderContext> =
			BuilderForCodecsImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		interface BuilderForCodecs<out Context : JSONCoderContext> {

			fun codecs(provider: JSONCodecProvider<Context>): BuilderForDestination<Context>


			fun codecs(
				vararg providers: JSONCodecProvider<Context>,
				appendBasic: Boolean = true
			) =
				codecs(JSONCodecProvider.of(providers = *providers, appendBasic = appendBasic))


			fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendBasic: Boolean = true
			) =
				codecs(JSONCodecProvider.of(providers = providers, appendBasic = appendBasic))
		}


		private class BuilderForCodecsImpl<out Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(provider: JSONCodecProvider<Context>) =
				BuilderForDestinationImpl(
					context = context,
					codecProvider = provider
				)
		}


		interface BuilderForDestination<out Context : JSONCoderContext> {

			fun destination(destination: JSONWriter): Builder<Context>


			fun destination(destination: Writer) =
				destination(JSONWriter.build(destination))
		}


		private class BuilderForDestinationImpl<out Context : JSONCoderContext>(
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


		interface Builder<out Context : JSONCoderContext> {

			fun build(): JSONEncoder<Context>
		}


		private class BuilderImpl<out Context : JSONCoderContext>(
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
