package com.github.fluidsonic.fluid.json

import java.io.StringWriter
import java.io.Writer


internal interface JSONSerializer {

	fun serialize(value: Any?) =
		StringWriter().apply { serialize(value, this) }.toString()

	fun serialize(value: Any?, destination: JSONWriter)

	fun serialize(value: Any?, destination: Writer)
		= serialize(value, JSON.writer(destination))
}
