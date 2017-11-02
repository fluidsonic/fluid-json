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
			.encodingWith(JSONCodecProvider.default)
			.build()


		interface BuilderForEncoding<Context : JSONCoderContext> {

			fun encodingWith(factory: (destination: Writer, context: Context) -> JSONEncoder<Context>): Builder


			private fun encodingWith(provider: JSONCodecProvider<Context>): Builder =
				encodingWith { destination, context ->
					JSONEncoder.builder(context)
						.codecs(provider)
						.destination(destination)
						.build()
				}


			fun encodingWith(
				vararg providers: JSONCodecProvider<Context>,
				appendBasic: Boolean = true
			) =
				encodingWith(JSONCodecProvider.of(providers = *providers, appendBasic = appendBasic))


			fun encodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendBasic: Boolean = true
			) =
				encodingWith(JSONCodecProvider.of(providers = providers, appendBasic = appendBasic))
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
