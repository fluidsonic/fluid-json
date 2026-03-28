package io.fluidsonic.json

import java.io.*


/**
 * Serializes values to JSON using a [JsonWriter].
 */
public interface JsonSerializer {

	public fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean = true)


	public companion object {

		public val default: JsonSerializer get() = StandardSerializer
	}
}


/** Serializes a value to a JSON [String]. */
public fun JsonSerializer.serializeValue(value: Any?): String =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()


/** Serializes a value as JSON to a [Writer] destination. */
public fun JsonSerializer.serializeValue(value: Any?, destination: Writer, withTermination: Boolean = true) {
	serializeValue(value, destination = JsonWriter.build(destination), withTermination = withTermination)
}
