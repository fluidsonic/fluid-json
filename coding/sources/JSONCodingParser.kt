package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONCodingParser : JSONParser {

	override fun parseList(source: JSONReader, finalizeAndClose: Boolean) =
		parseValueOfType<List<*>>(source, finalizeAndClose = finalizeAndClose)


	override fun parseMap(source: JSONReader, finalizeAndClose: Boolean) =
		parseValueOfType<Map<String, *>>(source, finalizeAndClose = finalizeAndClose)


	fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodingType<Value>, finalizeAndClose: Boolean = true): Value?


	override fun parseValueOrNull(source: JSONReader, finalizeAndClose: Boolean) =
		parseValueOfTypeOrNull<Any>(source, finalizeAndClose = finalizeAndClose)


	companion object {

		fun builder(): BuilderForDecoding<JSONCodingContext> =
			BuilderForDecodingImpl(context = JSONCodingContext.empty)


		fun <Context : JSONCodingContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		val default = builder()
			.decodingWith()
			.build()


		val nonRecursive = builder()
			.decodingWith(DefaultJSONCodecs.nonRecursive)
			.build()


		interface BuilderForDecoding<Context : JSONCodingContext> {

			fun decodingWith(factory: (source: JSONReader, context: Context) -> JSONDecoder<Context>): Builder


			fun decodingWith(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				decodingWith(providers = providers.toList(), base = base)


			fun decodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				decodingWith { source, context ->
					JSONDecoder.builder(context)
						.codecs(JSONCodecProvider.of(providers = providers, base = base))
						.source(source)
						.build()
				}
		}


		private class BuilderForDecodingImpl<Context : JSONCodingContext>(
			private val context: Context
		) : BuilderForDecoding<Context> {

			override fun decodingWith(factory: (source: JSONReader, context: Context) -> JSONDecoder<Context>) =
				BuilderImpl(
					context = context,
					decoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JSONCodingParser
		}


		private class BuilderImpl<out Context : JSONCodingContext>(
			private val context: Context,
			private val decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
		) : Builder {

			override fun build() =
				StandardCodingParser(
					context = context,
					decoderFactory = decoderFactory
				)
		}
	}
}


fun JSONCodingParser.parseValue(source: JSONReader, finalizeAndClose: Boolean = true) =
	parseValueOrNull(source, finalizeAndClose = finalizeAndClose) ?: throw JSONException("Unexpected null value at top-level")


fun JSONCodingParser.parseValue(source: Reader, finalizeAndClose: Boolean = true) =
	parseValue(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONCodingParser.parseValue(source: String) =
	parseValue(JSONReader.build(source))


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, finalizeAndClose: Boolean = true): Value =
	parseValueOfType(source, valueType = jsonCodingType(), finalizeAndClose = finalizeAndClose)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: Reader, finalizeAndClose: Boolean = true): Value =
	parseValueOfType(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: String): Value =
	parseValueOfType(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, valueType: JSONCodingType<Value>, finalizeAndClose: Boolean = true) =
	parseValueOfTypeOrNull(source, valueType = valueType, finalizeAndClose = finalizeAndClose) ?: throw JSONException("Unexpected null value at top-level")


fun <Value : Any> JSONCodingParser.parseValueOfType(source: Reader, valueType: JSONCodingType<Value>, finalizeAndClose: Boolean = true) =
	parseValueOfType(JSONReader.build(source), valueType = valueType, finalizeAndClose = finalizeAndClose)


fun <Value : Any> JSONCodingParser.parseValueOfType(source: String, valueType: JSONCodingType<Value>): Value =
	parseValueOfType(JSONReader.build(source), valueType = valueType)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: JSONReader, finalizeAndClose: Boolean = true): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodingType(), finalizeAndClose = finalizeAndClose)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, finalizeAndClose: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, valueType: JSONCodingType<Value>, finalizeAndClose: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType, finalizeAndClose = finalizeAndClose)


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String, valueType: JSONCodingType<Value>): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType)


fun JSONCodingParser.parseValueOrNull(source: Reader, finalizeAndClose: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONCodingParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))
