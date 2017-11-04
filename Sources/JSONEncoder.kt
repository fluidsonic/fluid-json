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

			fun codecs(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			) =
				codecs(providers = providers.toList(), base = base)


			fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			): BuilderForDestination<Context>
		}


		private class BuilderForCodecsImpl<out Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCoderContext>?
			) =
				BuilderForDestinationImpl(
					context = context,
					codecProvider = JSONCodecProvider.of(providers = providers, base = base)
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
