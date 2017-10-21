package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser<in Context : JSONCoderContext> {

	fun <Value : Any> parse(source: Reader, valueClass: Class<out Value>, context: Context): Value?


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
			decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
		): JSONParser<Context> =
			StandardParser(decoderFactory = decoderFactory)
	}
}


fun JSONParser<JSONCoderContext>.parse(source: String): Any? =
	parseOfType(source)


fun JSONParser<JSONCoderContext>.parse(source: Reader): Any? =
	parseOfType(source)


fun <Context : JSONCoderContext> JSONParser<Context>.parse(source: String, context: Context): Any? =
	parseOfType(source, context = context)


fun <Context : JSONCoderContext> JSONParser<Context>.parse(source: Reader, context: Context): Any? =
	parseOfType(source, context = context)


fun <Value : Any> JSONParser<JSONCoderContext>.parse(source: String, valueClass: Class<out Value>): Value? =
	parse(source, valueClass = valueClass, context = JSONCoderContext.empty)


fun <Value : Any> JSONParser<JSONCoderContext>.parse(source: Reader, valueClass: Class<out Value>): Value? =
	parse(source, valueClass = valueClass, context = JSONCoderContext.empty)


fun <Value : Any, Context : JSONCoderContext> JSONParser<Context>.parse(source: String, valueClass: Class<out Value>, context: Context): Value? =
	parse(StringReader(source), valueClass = valueClass, context = context)


inline fun <reified Value : Any> JSONParser<JSONCoderContext>.parseOfType(source: Reader): Value? =
	parseOfType(source, context = JSONCoderContext.empty)


inline fun <reified Value : Any> JSONParser<JSONCoderContext>.parseOfType(source: String): Value? =
	parseOfType(source, context = JSONCoderContext.empty)


inline fun <reified Value : Any, Context : JSONCoderContext> JSONParser<Context>.parseOfType(source: Reader, context: Context): Value? =
	parse(source, valueClass = Value::class.java, context = context)


inline fun <reified Value : Any, Context : JSONCoderContext> JSONParser<Context>.parseOfType(source: String, context: Context): Value? =
	parse(source, valueClass = Value::class.java, context = context)
