package io.fluidsonic.json

import java.io.*


interface JsonSerializer {

	fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean = true)


	companion object {

		val default: JsonSerializer get() = StandardSerializer
	}
}


fun JsonSerializer.serializeValue(value: Any?) =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()


fun JsonSerializer.serializeValue(value: Any?, destination: Writer, withTermination: Boolean = true) =
	serializeValue(value, destination = JsonWriter.build(destination), withTermination = withTermination)
