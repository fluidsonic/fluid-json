package com.github.fluidsonic.fluid.json

import java.io.Writer


interface JSONEncoder<out Context : JSONCoderContext> : JSONWriter {

	val context: Context

	fun writeEncodable(value: Any)


	override fun writeValue(value: Any?) =
		if (value != null) writeEncodable(value) else writeNull()


	override fun writeValueAsMapKey(value: Any?) =
		if (value != null) writeEncodable(value) else throw JSONException("Cannot write null as JSON map key")


	companion object {

		fun with(
			destination: JSONWriter,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		) =
			with(destination = destination, context = JSONCoderContext.empty, codecResolver = codecResolver)


		fun with(
			destination: Writer,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		) =
			with(destination = destination, context = JSONCoderContext.empty, codecResolver = codecResolver)


		fun <Context : JSONCoderContext> with(
			destination: JSONWriter,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONEncoder<Context> =
			StandardEncoder(codecResolver = codecResolver, context = context, destination = destination)


		fun <Context : JSONCoderContext> with(
			destination: Writer,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		) =
			with(destination = JSONWriter.with(destination), context = context, codecResolver = codecResolver)
	}
}


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
