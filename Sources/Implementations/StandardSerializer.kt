package com.github.fluidsonic.fluid.json

import java.io.Writer


internal class StandardSerializer<in Context : JSONCoderContext>(
	private val context: Context,
	private val encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<Context>
) : JSONSerializer<Context> {

	override fun serialize(value: Any?, destination: Writer) {
		val encoder = encoderFactory(destination, context)
		encoder.use {
			encoder.writeEncodableOrNull(value)
		}
	}


	override fun withContext(context: Context): JSONSerializer<Context> =
		StandardSerializer(context = context, encoderFactory = encoderFactory)
}
