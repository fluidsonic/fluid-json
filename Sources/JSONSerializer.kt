package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer {

	fun serializeValue(value: Any?, destination: Writer)


	companion object {


		fun builder(): BuilderForEncoding<JSONCoderContext> =
			BuilderForEncodingImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForEncoding<Context> =
			BuilderForEncodingImpl(context = context)


		val default = builder()
			.encodingWith(DefaultJSONCodecs.nonRecursive)
			.build()


		interface BuilderForEncoding<Context : JSONCoderContext> {

			fun encodingWith(factory: (destination: Writer, context: Context) -> JSONEncoder<Context>): Builder


			fun encodingWith(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			) =
				encodingWith(providers = providers.toList(), base = base)


			fun encodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCoderContext>? = JSONCodecProvider.extended
			) =
				encodingWith { destination, context ->
					JSONEncoder.builder(context)
						.codecs(JSONCodecProvider.of(providers = providers, base = base))
						.destination(destination)
						.build()
				}
		}


		private class BuilderForEncodingImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForEncoding<Context> {

			override fun encodingWith(factory: (source: Writer, context: Context) -> JSONEncoder<Context>) =
				BuilderImpl(
					context = context,
					encoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JSONSerializer
		}


		private class BuilderImpl<out Context : JSONCoderContext>(
			private val context: Context,
			private val encoderFactory: (source: Writer, context: Context) -> JSONEncoder<Context>
		) : Builder {

			override fun build() =
				StandardSerializer(
					context = context,
					encoderFactory = encoderFactory
				)
		}
	}
}


fun JSONSerializer.serializeValue(value: Any?) =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()
