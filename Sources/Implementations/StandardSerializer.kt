package com.github.fluidsonic.fluid.json

import java.io.Writer


internal class StandardSerializer<out Context : JSONCoderContext>(
	private val context: Context,
	private val encoderFactory: (destination: Writer, context: Context) -> JSONEncoder<Context>
) : JSONSerializer {

	override fun serializeValue(value: Any?, destination: Writer) {
		encoderFactory(destination, context).use { encoder ->
			encoder.writeValueOrNull(value)
		}
	}
}
