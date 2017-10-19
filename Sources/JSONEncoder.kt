package com.github.fluidsonic.fluid.json

import java.io.Writer


interface JSONEncoder<out Context : JSONCoderContext> : JSONWriter {

	val context: Context

	fun writeEncodable(value: Any)

	override fun writeValue(value: Any?) =
		if (value != null) writeEncodable(value) else writeNull()


	companion object {

		operator fun invoke(
			destination: Writer,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONEncoder<JSONCoderContext> =
			StandardEncoder(codecResolver = codecResolver, context = JSONCoderContext.empty, destination = JSONWriter(destination))


		operator fun invoke(
			destination: JSONWriter,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONEncoder<JSONCoderContext> =
			StandardEncoder(codecResolver = codecResolver, context = JSONCoderContext.empty, destination = destination)


		operator fun <Context : JSONCoderContext> invoke(
			destination: Writer,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONEncoder<Context> =
			StandardEncoder(codecResolver = codecResolver, context = context, destination = JSONWriter(destination))


		operator fun <Context : JSONCoderContext> invoke(
			destination: JSONWriter,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONEncoder<Context> =
			StandardEncoder(codecResolver = codecResolver, context = context, destination = destination)
	}
}

// FIXME what about writeMap keys? auto-encode too?


fun JSONEncoder<*>.writeEncodableOrNull(value: Any?) =
	if (value != null) writeEncodable(value) else writeNull()


fun JSONEncoder<*>.writeMapElement(key: String, encodable: Any) {
	writeMapKey(key)
	writeEncodable(encodable)
}


fun JSONEncoder<*>.writeMapElement(key: String, encodable: Any?, skipIfNull: Boolean = false) =
	if (encodable != null)
		writeMapElement(key, encodable)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
