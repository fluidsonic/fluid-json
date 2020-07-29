package io.fluidsonic.json

import java.io.*


public interface JsonSerializer {

	public fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean = true)


	public companion object {

		public val default: JsonSerializer get() = StandardSerializer
	}
}


public fun JsonSerializer.serializeValue(value: Any?): String =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()


public fun JsonSerializer.serializeValue(value: Any?, destination: Writer, withTermination: Boolean = true) {
	serializeValue(value, destination = JsonWriter.build(destination), withTermination = withTermination)
}
