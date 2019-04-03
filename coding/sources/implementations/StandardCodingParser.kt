package com.github.fluidsonic.fluid.json


internal class StandardCodingParser<out Context : JSONCodingContext>(
	private val context: Context,
	private val decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
) : JSONCodingParser<Context> {

	override fun createDecoder(source: JSONReader) =
		decoderFactory(source, context)
}
