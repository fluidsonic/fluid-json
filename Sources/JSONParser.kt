package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser<in Context : JSONCoderContext> {

	fun parse(source: JSONReader, context: Context): Any?


	companion object {

		private val default = with(codecResolver = JSONCodecResolver.plain)


		fun default() =
			default


		fun <Context : JSONCoderContext> with(
			codecResolver: JSONCodecResolver<Context>
		): JSONParser<Context> =
			with { source, context ->
				JSONDecoder.with(
					context = context,
					codecResolver = codecResolver,
					source = source
				)
			}


		fun <Context : JSONCoderContext> with(
			decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
		): JSONParser<Context> =
			StandardParser(decoderFactory = decoderFactory)
	}
}


fun JSONParser<JSONCoderContext>.parse(source: String) =
	parse(source, context = JSONCoderContext.empty)


fun <Context : JSONCoderContext> JSONParser<Context>.parse(source: String, context: Context) =
	parse(StringReader(source), context = context)


fun JSONParser<JSONCoderContext>.parse(source: JSONReader) =
	parse(source, context = JSONCoderContext.empty)


fun JSONParser<JSONCoderContext>.parse(source: Reader) =
	parse(source, context = JSONCoderContext.empty)


fun <Context : JSONCoderContext> JSONParser<Context>.parse(source: Reader, context: Context) =
	parse(JSONReader.with(source), context = context)
