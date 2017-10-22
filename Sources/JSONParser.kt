package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser<Context : JSONCoderContext> {

	val context: Context


	fun <Value : Any> doParseWithClass(
		source: Reader,
		valueClass: Class<out Value>
	): Value?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>
	): List<Value>?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Value
	): List<Value?>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>
	): Map<Key, Value>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Key
	): Map<Key?, Value>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.KeyAndValue
	): Map<Key?, Value?>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Value
	): Map<Key, Value?>?


	fun <NewContext : Context> withContext(context: NewContext): JSONParser<NewContext>


	companion object {

		private val default = builder()
			.decoder(AnyJSONCodec)
			.build()


		fun builder(): BuilderForDecoder<JSONCoderContext> =
			BuilderForDecoderImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForDecoder<Context> =
			BuilderForDecoderImpl(context = context)


		fun default() =
			default


		interface BuilderForDecoder<Context : JSONCoderContext> {

			fun decoder(factory: (source: Reader, context: Context) -> JSONDecoder<Context>): Builder<Context>


			fun decoder(resolver: JSONCodecResolver<Context>) =
				decoder { source, context ->
					JSONDecoder.builder(context)
						.codecs(resolver)
						.source(source)
						.build()
				}


			fun decoder(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				decoder(JSONCodecResolver.of(providers = *providers, appendDefaultCodecs = appendDefaultCodecs))


			fun decoder(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				decoder(JSONCodecResolver.of(providers = providers, appendDefaultCodecs = appendDefaultCodecs))
		}


		private class BuilderForDecoderImpl<Context : JSONCoderContext>(
			private val context: Context
		) : BuilderForDecoder<Context> {

			override fun decoder(factory: (source: Reader, context: Context) -> JSONDecoder<Context>) =
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


fun <Value : Any> JSONParser<*>.doParseWithClass(
	source: String,
	valueClass: Class<out Value>
): Value? =
	doParseWithClass(StringReader(source), valueClass = valueClass)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: Class<out Value>
): List<Value>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: Class<out Value>,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: Class<out Key>,
	valueClass: Class<out Value>
): Map<Key, Value>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: Class<out Key>,
	valueClass: Class<out Value>,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: Class<out Key>,
	valueClass: Class<out Value>,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: Class<out Key>,
	valueClass: Class<out Value>,
	nullability: JSONNullability.Value
): Map<Key, Value?>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


fun JSONParser<*>.parseList(
	source: Reader
) =
	parseListOfType<Any>(source)


fun JSONParser<*>.parseList(
	source: Reader,
	nullability: JSONNullability.Value
) =
	parseListOfType<Any>(source, nullability = nullability)


fun JSONParser<*>.parseList(
	source: String
) =
	parseListOfType<Any>(source)


fun JSONParser<*>.parseList(
	source: String,
	nullability: JSONNullability.Value
) =
	parseListOfType<Any>(source, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: Reader
): List<Value>? =
	doParseListWithClass(source, valueClass = Value::class.java)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: Reader,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String
): List<Value>? =
	doParseListWithClass(source, valueClass = Value::class.java)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: Reader
) =
	parseMapOfType<String, Any>(source)


fun JSONParser<*>.parseMap(
	source: Reader,
	nullability: JSONNullability.Key
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: Reader,
	nullability: JSONNullability.KeyAndValue
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: Reader,
	nullability: JSONNullability.Value
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: String
) =
	parseMapOfType<String, Any>(source)


fun JSONParser<*>.parseMap(
	source: String,
	nullability: JSONNullability.Key
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: String,
	nullability: JSONNullability.KeyAndValue
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: String,
	nullability: JSONNullability.Value
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader
): Map<Key, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.Value
): Map<Key, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String
): Map<Key, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.Value
): Map<Key, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


fun JSONParser<*>.parseValue(
	source: Reader
) =
	parseValueOfType<Any>(source)


fun JSONParser<*>.parseValue(
	source: String
) =
	parseValueOfType<Any>(source)


inline fun <reified Value : Any> JSONParser<*>.parseValueOfType(
	source: Reader
): Value? =
	doParseWithClass(source, valueClass = Value::class.java)


inline fun <reified Value : Any> JSONParser<*>.parseValueOfType(
	source: String
): Value? =
	doParseWithClass(source, valueClass = Value::class.java)
