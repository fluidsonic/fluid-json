package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer<in Context : JSONCoderContext> {

	fun serialize(value: Any?, destination: Writer)

	fun withContext(context: Context): JSONSerializer<Context>


	companion object {

		private val default = with(codecResolver = JSONCodecResolver.plain)


		fun default() =
			default


		fun with(
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONSerializer<JSONCoderContext> =
			with(context = JSONCoderContext.empty, codecResolver = codecResolver)


		fun <Context : JSONCoderContext> with(
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONSerializer<Context> =
			with(context) { destination, overridingContext ->
				JSONEncoder.with(
					context = overridingContext,
					codecResolver = codecResolver,
					destination = destination
				)
			}


		fun with(
			encoderFactory: (destination: Writer, context: JSONCoderContext) -> JSONEncoder<JSONCoderContext>
		): JSONSerializer<JSONCoderContext> =
			with(context = JSONCoderContext.empty, encoderFactory = encoderFactory)


		fun <Context : JSONCoderContext> with(
			context: Context,
			encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<Context>
		): JSONSerializer<Context> =
			StandardSerializer(context = context, encoderFactory = encoderFactory)
	}
}


fun JSONSerializer<*>.serialize(value: Any?) =
	StringWriter().apply { serialize(value, destination = this) }.toString()
