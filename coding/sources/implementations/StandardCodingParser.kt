package io.fluidsonic.json


internal class StandardCodingParser<out Context : JsonCodingContext>(
	private val context: Context,
	private val decoderFactory: (source: JsonReader, context: Context) -> JsonDecoder<Context>
) : JsonCodingParser<Context> {

	override fun createDecoder(source: JsonReader) =
		decoderFactory(source, context)
}
