package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser<Context : JSONCoderContext> {

	val context: Context


	fun <Value : Any> parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>): Value?


	fun <NewContext : Context> withContext(context: NewContext): JSONParser<NewContext>


	companion object {

		private val default = builder()
			.decodingWith(JSONCodecProvider.default, appendDefault = false)
			.build()


		private val nonRecursive = builder()
			.decodingWith(JSONCodecProvider.nonRecursive, appendDefault = false)
			.build()


		fun builder(): BuilderForDecoding<JSONCoderContext> =
			BuilderForDecodingImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		fun default() =
			default


		fun nonRecursive() =
			nonRecursive


		interface BuilderForDecoding<Context : JSONCoderContext> {

			fun decodingWith(factory: (source: Reader, context: Context) -> JSONDecoder<Context>): Builder<Context>


			private fun decodingWith(
				provider: JSONCodecProvider<Context>
			) =
				decodingWith { source, context ->
					JSONDecoder.builder(context)
						.codecs(provider)
						.source(source)
						.build()
				}


			fun decodingWith(
				vararg providers: JSONCodecProvider<Context>,
				appendDefault: Boolean = true
			) =
				decodingWith(JSONCodecProvider.of(providers = *providers, appendDefault = appendDefault))


			fun decodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefault: Boolean = true
			) =
				decodingWith(JSONCodecProvider.of(providers = providers, appendDefault = appendDefault))
		}


		private class BuilderForDecodingImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForDecoding<Context> {

			override fun decodingWith(factory: (source: Reader, context: Context) -> JSONDecoder<Context>) =
				BuilderImpl(
					context = context,
					decoderFactory = factory
				)
		}


		interface Builder<Context : JSONCoderContext> {

			fun build(): JSONParser<Context>
		}


		private class BuilderImpl<Context : JSONCoderContext>(
			private val context: Context,
			private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
		) : Builder<Context> {

			override fun build() =
				StandardParser(
					context = context,
					decoderFactory = decoderFactory
				)
		}
	}
}


fun JSONParser<*>.parseValue(source: Reader) =
	parseValueOrNull(source) ?: throw JSONException("Unexpected null value at top-level")


fun JSONParser<*>.parseValue(source: String) =
	parseValue(StringReader(source))


inline fun <reified Value : Any> JSONParser<*>.parseValueOfType(source: Reader): Value =
	parseValueOfType(source, valueType = jsonCodableType())


inline fun <reified Value : Any> JSONParser<*>.parseValueOfType(source: String): Value =
	parseValueOfType(StringReader(source))


fun <Value : Any> JSONParser<*>.parseValueOfType(source: Reader, valueType: JSONCodableType<Value>) =
	parseValueOfTypeOrNull(source, valueType = valueType) ?: throw JSONException("Unexpected null value at top-level")


fun <Value : Any> JSONParser<*>.parseValueOfType(source: String, valueType: JSONCodableType<Value>): Value =
	parseValueOfType(StringReader(source), valueType = valueType)


inline fun <reified Value : Any> JSONParser<*>.parseValueOfTypeOrNull(source: Reader): Value? =
	parseValueOfTypeOrNull(source, valueType = jsonCodableType())


inline fun <reified Value : Any> JSONParser<*>.parseValueOfTypeOrNull(source: String): Value? =
	parseValueOfTypeOrNull(StringReader(source))


fun <Value : Any> JSONParser<*>.parseValueOfTypeOrNull(source: String, valueType: JSONCodableType<Value>): Value? =
	parseValueOfTypeOrNull(StringReader(source), valueType = valueType)


fun JSONParser<*>.parseValueOrNull(source: Reader) =
	parseValueOfTypeOrNull<Any>(source)


fun JSONParser<*>.parseValueOrNull(source: String) =
	parseValueOrNull(StringReader(source))
