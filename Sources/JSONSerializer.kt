package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer<Context : JSONCoderContext> {

	val context: Context


	fun serializeValue(value: Any?, destination: Writer)

	fun <NewContext : Context> withContext(context: NewContext): JSONSerializer<NewContext>


	companion object {

		private val default = builder()
			.encodingWith(AnyJSONCodec)
			.build()


		fun builder(): BuilderForEncoding<JSONCoderContext> =
			BuilderForDecodingImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForEncoding<Context> =
			BuilderForDecodingImpl(context = context)


		fun default() =
			JSONSerializer.default


		interface BuilderForEncoding<Context : JSONCoderContext> {

			fun encodingWith(factory: (destination: Writer, context: Context) -> JSONEncoder<Context>): Builder<Context>


			fun encodingWith(resolver: JSONCodecResolver<Context>) =
				encodingWith { destination, context ->
					JSONEncoder.builder(context)
						.codecs(resolver)
						.destination(destination)
						.build()
				}


			fun encodingWith(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				encodingWith(JSONCodecResolver.of(providers = *providers, appendDefaultCodecs = appendDefaultCodecs))


			fun encodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				encodingWith(JSONCodecResolver.of(providers = providers, appendDefaultCodecs = appendDefaultCodecs))
		}


		private class BuilderForDecodingImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForEncoding<Context> {

			override fun encodingWith(factory: (source: Writer, context: Context) -> JSONEncoder<Context>) =
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
