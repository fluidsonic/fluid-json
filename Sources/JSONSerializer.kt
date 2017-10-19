package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


interface JSONSerializer {

	fun serialize(value: Any?) =
		StringWriter().apply { serialize(value, destination = this) }.toString()

	fun serialize(value: Any?, destination: JSONWriter)

	fun serialize(value: Any?, destination: Writer)
		= serialize(value, destination = JSONWriter(destination))


	companion object {

		operator fun invoke(): JSONSerializer =
			StandardSerializer.default
	}
}
