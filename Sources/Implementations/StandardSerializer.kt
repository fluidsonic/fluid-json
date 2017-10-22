package com.github.fluidsonic.fluid.json

import java.io.Writer


internal class StandardSerializer<Context : JSONCoderContext>(
	override val context: Context,
	private val encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<in Context>
) : JSONSerializer<Context> {

	override fun serializeValue(value: Any?, destination: Writer) {
		val encoder = encoderFactory(destination, context)
		encoder.use {
			encoder.writeEncodableOrNull(value)
		}
	}


	override fun <NewContext : Context> withContext(context: NewContext) =
		StandardSerializer(context = context, encoderFactory = encoderFactory)
}
