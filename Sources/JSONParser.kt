package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader
import kotlin.reflect.KClass


interface JSONParser<Context : JSONCoderContext> {

	val context: Context


	fun <Value : Any> doParseWithClass(
		source: Reader,
		valueClass: KClass<out Value>
	): Value?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: KClass<out Value>
	): List<Value>?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: KClass<out Value>,
		nullability: JSONNullability.Value
	): List<Value?>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: KClass<out Key>,
		valueClass: KClass<out Value>
	): Map<Key, Value>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: KClass<out Key>,
		valueClass: KClass<out Value>,
		nullability: JSONNullability.Key
	): Map<Key?, Value>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: KClass<out Key>,
		valueClass: KClass<out Value>,
		nullability: JSONNullability.KeyAndValue
	): Map<Key?, Value?>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: KClass<out Key>,
		valueClass: KClass<out Value>,
		nullability: JSONNullability.Value
	): Map<Key, Value?>?


	fun <NewContext : Context> withContext(context: NewContext): JSONParser<NewContext>


	companion object {

		private val default = builder()
			.decodingWith(AnyJSONCodec)
			.build()


		fun builder(): BuilderForDecoding<JSONCoderContext> =
			BuilderForDecodingImpl(context = JSONCoderContext.empty)


		fun <Context : JSONCoderContext> builder(context: Context): BuilderForDecoding<Context> =
			BuilderForDecodingImpl(context = context)


		fun default() =
			default


		interface BuilderForDecoding<Context : JSONCoderContext> {

			fun decodingWith(factory: (source: Reader, context: Context) -> JSONDecoder<Context>): Builder<Context>


			fun decodingWith(resolver: JSONCodecResolver<Context>) =
				decodingWith { source, context ->
					JSONDecoder.builder(context)
						.codecs(resolver)
						.source(source)
						.build()
				}


			fun decodingWith(
				vararg providers: JSONCodecProvider<Context>,
				appendDefaultCodecs: Boolean = true
			) =
				decodingWith(JSONCodecResolver.of(providers = *providers, appendDefaultCodecs = appendDefaultCodecs))


			fun decodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				appendDefaultCodecs: Boolean = true
			) =
				decodingWith(JSONCodecResolver.of(providers = providers, appendDefaultCodecs = appendDefaultCodecs))
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


fun <Value : Any> JSONParser<*>.doParseWithClass(
	source: String,
	valueClass: KClass<out Value>
): Value? =
	doParseWithClass(StringReader(source), valueClass = valueClass)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: KClass<out Value>
): List<Value>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: KClass<out Value>,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: KClass<out Key>,
	valueClass: KClass<out Value>
): Map<Key, Value>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: KClass<out Key>,
	valueClass: KClass<out Value>,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: KClass<out Key>,
	valueClass: KClass<out Value>,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: KClass<out Key>,
	valueClass: KClass<out Value>,
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
	doParseListWithClass(source, valueClass = Value::class)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: Reader,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String
): List<Value>? =
	doParseListWithClass(source, valueClass = Value::class)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class, nullability = nullability)


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
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: Reader,
	nullability: JSONNullability.Value
): Map<Key, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String
): Map<Key, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.Key
): Map<Key?, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.KeyAndValue
): Map<Key?, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


inline fun <reified Key : Any, reified Value : Any> JSONParser<*>.parseMapOfType(
	source: String,
	nullability: JSONNullability.Value
): Map<Key, Value?>? =
	doParseMapWithClasses(source, keyClass = Key::class, valueClass = Value::class, nullability = nullability)


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
	doParseWithClass(source, valueClass = Value::class)


inline fun <reified Value : Any> JSONParser<*>.parseValueOfType(
	source: String
): Value? =
	doParseWithClass(source, valueClass = Value::class)
