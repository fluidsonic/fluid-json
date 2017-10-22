package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer<Context : JSONCoderContext> {

	val context: Context


	fun serializeValue(value: Any?, destination: Writer)

	fun <NewContext : Context> withContext(context: NewContext): JSONSerializer<NewContext>


	companion object {

		private val default = builder()
			.encoder(AnyJSONCodec)
			.build()


		fun builder(): BuilderForEncoder<JSONCoderContext> =
			BuilderForDecoderImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForEncoder<Context> =
			BuilderForDecoderImpl(context = context)


		fun default() =
			JSONSerializer.default


		interface BuilderForEncoder<Context : JSONCoderContext> {

			fun encoder(factory: (destination: Writer, context: Context) -> JSONEncoder<Context>): Builder<Context>


			fun encoder(resolver: JSONCodecResolver<Context>) =
				encoder { destination, context ->
					JSONEncoder.builder(context)
						.codecs(resolver)
						.destination(destination)
						.build()
				}


			fun encoder(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				encoder(JSONCodecResolver.of(providers = *providers, appendDefaultCodecs = appendDefaultCodecs))


			fun encoder(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				encoder(JSONCodecResolver.of(providers = providers, appendDefaultCodecs = appendDefaultCodecs))
		}


		private class BuilderForDecoderImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForEncoder<Context> {

			override fun encoder(factory: (source: Writer, context: Context) -> JSONEncoder<Context>) =
				BuilderImpl(
					context = context,
					encoderFactory = factory
				)
		}


		interface Builder<Context : JSONCoderContext> {

			fun build(): JSONSerializer<Context>
		}


		private class BuilderImpl<Context : JSONCoderContext>(
			private val context: Context,
			private val encoderFactory: (source: Writer, context: Context) -> JSONEncoder<Context>
		) : Builder<Context> {

			override fun build() =
				StandardSerializer(
					context = context,
					encoderFactory = encoderFactory
				)
		}
	}
}


fun JSONSerializer<*>.serializeValue(value: Any?) =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()
