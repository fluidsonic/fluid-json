package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer<in Context : JSONCoderContext> {

	fun serialize(value: Any?, destination: Writer, context: Context)


	companion object {

		private val default = with(codecResolver = JSONCodecResolver.plain)


		fun default() =
			default


		fun <Context : JSONCoderContext> with(
			codecResolver: JSONCodecResolver<Context>
		): JSONSerializer<Context> =
			with { destination, context ->
				JSONEncoder.with(
					context = context,
					codecResolver = codecResolver,
					destination = destination
				)
			}


		fun <Context : JSONCoderContext> with(
			encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<Context>
		): JSONSerializer<Context> =
			StandardSerializer(encoderFactory = encoderFactory)
	}
}


fun JSONSerializer<JSONCoderContext>.serialize(value: Any?) =
	StringWriter().apply { serialize(value, destination = this, context = JSONCoderContext.empty) }.toString()


fun <Context : JSONCoderContext> JSONSerializer<Context>.serialize(value: Any?, context: Context) =
	StringWriter().apply { serialize(value, destination = this, context = context) }.toString()


fun JSONSerializer<JSONCoderContext>.serialize(value: Any?, destination: Writer) =
	serialize(value, destination = destination, context = JSONCoderContext.empty)
