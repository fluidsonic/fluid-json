package com.github.fluidsonic.fluid.json

import java.io.Writer


@Suppress("AddVarianceModifier")
interface JSONEncoder<Context : JSONCoderContext> : JSONWriter {

	val context: Context


	fun writeEncodable(value: Any)


	override fun writeValue(value: Any?) =
		if (value != null) writeEncodable(value) else writeNull()


	override fun writeValueAsMapKey(value: Any?) =
		if (value != null) writeEncodable(value) else throw JSONException("Cannot write null as JSON map key")


	companion object {

		fun builder(): BuilderForCodecs<JSONCoderContext> =
			BuilderForCodecsImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForCodecs<Context> =
			BuilderForCodecsImpl(context = context)


		interface BuilderForCodecs<Context : JSONCoderContext> {

			fun codecs(resolver: JSONCodecResolver<Context>): BuilderForDestination<Context>


			fun codecs(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				codecs(JSONCodecResolver.of(providers = *providers, appendDefaultCodecs = appendDefaultCodecs))


			fun codecs(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				codecs(JSONCodecResolver.of(providers = providers, appendDefaultCodecs = appendDefaultCodecs))
		}


		private class BuilderForCodecsImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForCodecs<Context> {

			override fun codecs(resolver: JSONCodecResolver<Context>) =
				BuilderForDestinationImpl(
					context = context,
					codecResolver = resolver
				)
		}


		interface BuilderForDestination<Context : JSONCoderContext> {

			fun destination(destination: JSONWriter): Builder<Context>


			fun destination(destination: Writer) =
				destination(JSONWriter.build(destination))
		}


		private class BuilderForDestinationImpl<Context : JSONCoderContext>(
			private val context: Context,
			private val codecResolver: JSONCodecResolver<Context>
		) : BuilderForDestination<Context> {

			override fun destination(destination: JSONWriter) =
				BuilderImpl(
					context = context,
					codecResolver = codecResolver,
					destination = destination
				)
		}


		interface Builder<Context : JSONCoderContext> {

			fun build(): JSONEncoder<Context>
		}


		private class BuilderImpl<Context : JSONCoderContext>(
			private val context: Context,
			private val codecResolver: JSONCodecResolver<Context>,
			private val destination: JSONWriter
		) : Builder<Context> {

			override fun build() =
				StandardEncoder(
					context = context,
					codecResolver = codecResolver,
					destination = destination
				)
		}
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
