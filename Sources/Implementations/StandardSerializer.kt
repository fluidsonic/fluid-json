package com.github.fluidsonic.fluid.json


internal class StandardSerializer<in Context : JSONCoderContext>(
	private val encoderFactory: (destination: JSONWriter, context: Context) -> JSONEncoder<Context>
) : JSONSerializer<Context> {

	override fun serialize(value: Any?, destination: JSONWriter, context: Context) {
		val encoder = encoderFactory(destination, context)
		encoder.use {
			encoder.writeEncodableOrNull(value)
		}
	}
}
