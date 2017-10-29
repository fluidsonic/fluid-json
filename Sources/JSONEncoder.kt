package com.github.fluidsonic.fluid.json

import java.io.Writer


@Suppress("AddVarianceModifier")
interface JSONEncoder<Context : JSONCoderContext> : JSONWriter {

	val context: Context


	companion object {

		fun builder(): BuilderForCodecs<JSONCoderContext> =
			BuilderForCodecsImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		interface BuilderForCodecs<Context : JSONCoderContext> {

			fun codecs(provider: JSONCodecProvider<Context>): BuilderForDestination<Context>


			fun codecs(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				codecs(JSONCodecProvider.of(providers = *providers, appendDefault = appendDefaultCodecs))


			fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				codecs(JSONCodecProvider.of(providers = providers, appendDefault = appendDefaultCodecs))
		}


		private class BuilderForCodecsImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(provider: JSONCodecProvider<Context>) =
				BuilderForDestinationImpl(
					context = context,
					codecProvider = provider
				)
		}


		interface BuilderForDestination<Context : JSONCoderContext> {

			fun destination(destination: JSONWriter): Builder<Context>


			fun destination(destination: Writer) =
				destination(JSONWriter.build(destination))
		}


		private class BuilderForDestinationImpl<Context : JSONCoderContext>(
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


		interface Builder<Context : JSONCoderContext> {

			fun build(): JSONEncoder<Context>
		}


		private class BuilderImpl<Context : JSONCoderContext>(
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
