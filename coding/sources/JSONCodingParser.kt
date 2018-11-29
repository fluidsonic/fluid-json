package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONCodingParser : JSONParser {

	override fun parseList(source: JSONReader, withTermination: Boolean) =
		parseValueOfType<List<*>>(source, withTermination = withTermination)


	override fun parseMap(source: JSONReader, withTermination: Boolean) =
		parseValueOfType<Map<String, *>>(source, withTermination = withTermination)


	fun <Value : Any> parseValueOfTypeOrNull(source: JSONReader, valueType: JSONCodingType<Value>, withTermination: Boolean = true): Value?


	override fun parseValueOrNull(source: JSONReader, withTermination: Boolean) =
		parseValueOfTypeOrNull<Any>(source, withTermination = withTermination)


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


fun JSONCodingParser.parseValue(source: JSONReader, withTermination: Boolean = true) =
	parseValueOrNull(source, withTermination = withTermination) ?: throw JSONException("Unexpected null value at top-level")


fun JSONCodingParser.parseValue(source: Reader, withTermination: Boolean = true) =
	parseValue(JSONReader.build(source), withTermination = withTermination)


fun JSONCodingParser.parseValue(source: String) =
	parseValue(JSONReader.build(source))


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, withTermination: Boolean = true): Value =
	parseValueOfType(source, valueType = jsonCodingType(), withTermination = withTermination)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: Reader, withTermination: Boolean = true): Value =
	parseValueOfType(JSONReader.build(source), withTermination = withTermination)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfType(source: String): Value =
	parseValueOfType(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfType(source: JSONReader, valueType: JSONCodingType<Value>, withTermination: Boolean = true) =
	parseValueOfTypeOrNull(source, valueType = valueType, withTermination = withTermination) ?: throw JSONException("Unexpected null value at top-level")


fun <Value : Any> JSONCodingParser.parseValueOfType(source: Reader, valueType: JSONCodingType<Value>, withTermination: Boolean = true) =
	parseValueOfType(JSONReader.build(source), valueType = valueType, withTermination = withTermination)


fun <Value : Any> JSONCodingParser.parseValueOfType(source: String, valueType: JSONCodingType<Value>): Value =
	parseValueOfType(JSONReader.build(source), valueType = valueType)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: JSONReader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodingType(), withTermination = withTermination)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), withTermination = withTermination)


inline fun <reified Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source))


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: Reader, valueType: JSONCodingType<Value>, withTermination: Boolean = true): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType, withTermination = withTermination)


fun <Value : Any> JSONCodingParser.parseValueOfTypeOrNull(source: String, valueType: JSONCodingType<Value>): Value? =
	parseValueOfTypeOrNull(JSONReader.build(source), valueType = valueType)


fun JSONCodingParser.parseValueOrNull(source: Reader, withTermination: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), withTermination = withTermination)


fun JSONCodingParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))
