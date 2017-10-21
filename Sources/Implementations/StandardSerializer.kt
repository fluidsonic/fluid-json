package com.github.fluidsonic.fluid.json

import java.io.Writer


internal class StandardSerializer<in Context : JSONCoderContext>(
	private val encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<Context>
) : JSONSerializer<Context> {

	override fun serialize(value: Any?, destination: Writer, context: Context) {
		val encoder = encoderFactory(destination, context)
		encoder.use {
			encoder.writeEncodableOrNull(value)
		}
	}
}
