package com.github.fluidsonic.fluid.json


internal class StandardCodingSerializer<out Context : JSONCodingContext>(
	private val context: Context,
	private val encoderFactory: (destination: JSONWriter, context: Context) -> JSONEncoder<Context>
) : JSONCodingSerializer {

	override fun serializeValue(value: Any?, destination: JSONWriter) {
		encoderFactory(destination, context).use { encoder ->
			encoder.writeValueOrNull(value)
		}
	}
}
