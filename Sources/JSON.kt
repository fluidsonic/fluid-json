package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader
import java.io.Writer


// FIXME api annotations everywhere?
internal object JSON {

	fun decoder(
		source: String,
		codecResolver: JSONCodecResolver<JSONCoderContext>
	): JSONDecoder<JSONCoderContext> =
		SimpleDecoder(codecResolver = codecResolver, context = JSONCoderContext.Empty, source = reader(source))


	fun decoder(
		source: Reader,
		codecResolver: JSONCodecResolver<JSONCoderContext>
	): JSONDecoder<JSONCoderContext> =
		SimpleDecoder(codecResolver = codecResolver, context = JSONCoderContext.Empty, source = reader(source))


	fun decoder(
		source: JSONReader,
		codecResolver: JSONCodecResolver<JSONCoderContext>
	): JSONDecoder<JSONCoderContext> =
		SimpleDecoder(codecResolver = codecResolver, context = JSONCoderContext.Empty, source = source)


	fun <Context : JSONCoderContext> decoder(
		source: String,
		context: Context,
		codecResolver: JSONCodecResolver<Context>
	): JSONDecoder<Context> =
		SimpleDecoder(codecResolver = codecResolver, context = context, source = reader(source))


	fun <Context : JSONCoderContext> decoder(
		source: Reader,
		context: Context,
		codecResolver: JSONCodecResolver<Context>
	): JSONDecoder<Context> =
		SimpleDecoder(codecResolver = codecResolver, context = context, source = reader(source))


	fun <Context : JSONCoderContext> decoder(
		source: JSONReader,
		context: Context,
		codecResolver: JSONCodecResolver<Context>
	): JSONDecoder<Context> =
		SimpleDecoder(codecResolver = codecResolver, context = context, source = source)


	fun encoder(
		destination: Writer,
		codecResolver: JSONCodecResolver<JSONCoderContext>
	): JSONEncoder<JSONCoderContext> =
		SimpleEncoder(codecResolver = codecResolver, context = JSONCoderContext.Empty, destination = writer(destination))


	fun encoder(
		destination: JSONWriter,
		codecResolver: JSONCodecResolver<JSONCoderContext>
	): JSONEncoder<JSONCoderContext> =
		SimpleEncoder(codecResolver = codecResolver, context = JSONCoderContext.Empty, destination = destination)


	fun <Context : JSONCoderContext> encoder(
		destination: Writer,
		context: Context,
		codecResolver: JSONCodecResolver<Context>
	): JSONEncoder<Context> =
		SimpleEncoder(codecResolver = codecResolver, context = context, destination = writer(destination))


	fun <Context : JSONCoderContext> encoder(
		destination: JSONWriter,
		context: Context,
		codecResolver: JSONCodecResolver<Context>
	): JSONEncoder<Context> =
		SimpleEncoder(codecResolver = codecResolver, context = context, destination = destination)


	fun parser(): JSONParser =
		SimpleParser


	fun reader(source: Reader): JSONReader =
		TextInputReader(TextInput(source))


	fun reader(source: String) =
		reader(StringReader(source))


	fun serializer(): JSONSerializer =
		SimpleSerializer.strict


	fun writer(destination: Writer): JSONWriter =
		TextOutputWriter(destination)
}
