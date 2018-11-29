package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer {

	fun serializeValue(value: Any?, destination: JSONWriter, withTermination: Boolean = true)


	companion object {

		val default: JSONSerializer get() = StandardSerializer
	}
}


fun JSONSerializer.serializeValue(value: Any?) =
	StringWriter().apply { serializeValue(value, destination = this) }.toString()


fun JSONSerializer.serializeValue(value: Any?, destination: Writer, withTermination: Boolean = true) =
	serializeValue(value, destination = JSONWriter.build(destination), withTermination = withTermination)
