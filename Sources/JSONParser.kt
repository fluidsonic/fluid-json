package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


interface JSONParser<Context : JSONCoderContext> {

	fun <Value : Any> doParseWithClass(
		source: Reader,
		valueClass: Class<out Value>
	): Value?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>,
		nullability: JSONNullability.None = JSONNullability.None
	): List<Value>?


	fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Value
	): List<Value?>?


	fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.None = JSONNullability.None
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


	fun withContext(context: Context): JSONParser<Context>


	companion object {

		private val default = with(
			context = JSONCoderContext.empty,
			codecResolver = JSONCodecResolver.plain
		)


		fun default() =
			default


		fun with(
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONParser<JSONCoderContext> =
			with(context = JSONCoderContext.empty, codecResolver = codecResolver)


		fun <Context : JSONCoderContext> with(
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONParser<Context> =
			with(context) { source, overridingContext ->
				JSONDecoder.with(
					context = overridingContext,
					codecResolver = codecResolver,
					source = source
				)
			}


		fun with(
			decoderFactory: (source: Reader, context: JSONCoderContext) -> JSONDecoder<JSONCoderContext>
		): JSONParser<JSONCoderContext> =
			with(context = JSONCoderContext.empty, decoderFactory = decoderFactory)


		fun <Context : JSONCoderContext> with(
			context: Context,
			decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
		): JSONParser<Context> =
			StandardParser(context = context, decoderFactory = decoderFactory)
	}
}


fun <Value : Any> JSONParser<*>.doParseWithClass(
	source: String,
	valueClass: Class<out Value>
): Value? =
	doParseWithClass(StringReader(source), valueClass = valueClass)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: Class<out Value>,
	nullability: JSONNullability.None = JSONNullability.None
): List<Value>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass, nullability = nullability)


fun <Value : Any> JSONParser<*>.doParseListWithClass(
	source: String,
	valueClass: Class<out Value>,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(StringReader(source), valueClass = valueClass, nullability = nullability)


fun <Key : Any, Value : Any> JSONParser<*>.doParseMapWithClasses(
	source: String,
	keyClass: Class<out Key>,
	valueClass: Class<out Value>,
	nullability: JSONNullability.None = JSONNullability.None
): Map<Key, Value>? =
	doParseMapWithClasses(StringReader(source), keyClass = keyClass, valueClass = valueClass, nullability = nullability)


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


fun JSONParser<*>.parse(
	source: Reader
) =
	parseOfType<Any>(source)


fun JSONParser<*>.parse(
	source: String
) =
	parseOfType<Any>(source)


fun JSONParser<*>.parseList(
	source: Reader,
	nullability: JSONNullability.None = JSONNullability.None
) =
	parseListOfType<Any>(source, nullability = nullability)


fun JSONParser<*>.parseList(
	source: Reader,
	nullability: JSONNullability.Value
) =
	parseListOfType<Any>(source, nullability = nullability)


fun JSONParser<*>.parseList(
	source: String,
	nullability: JSONNullability.None = JSONNullability.None
) =
	parseListOfType<Any>(source, nullability = nullability)


fun JSONParser<*>.parseList(
	source: String,
	nullability: JSONNullability.Value
) =
	parseListOfType<Any>(source, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: Reader,
	nullability: JSONNullability.None = JSONNullability.None
): List<Value>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: Reader,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String,
	nullability: JSONNullability.None = JSONNullability.None
): List<Value>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


inline fun <reified Value : Any> JSONParser<*>.parseListOfType(
	source: String,
	nullability: JSONNullability.Value
): List<Value?>? =
	doParseListWithClass(source, valueClass = Value::class.java, nullability = nullability)


fun JSONParser<*>.parseMap(
	source: Reader,
	nullability: JSONNullability.None = JSONNullability.None
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


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
	source: String,
	nullability: JSONNullability.None = JSONNullability.None
) =
	parseMapOfType<String, Any>(source, nullability = nullability)


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
	source: Reader,
	nullability: JSONNullability.None = JSONNullability.None
): Map<Key, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


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
	source: String,
	nullability: JSONNullability.None = JSONNullability.None
): Map<Key, Value>? =
	doParseMapWithClasses(source, keyClass = Key::class.java, valueClass = Value::class.java, nullability = nullability)


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


inline fun <reified Value : Any> JSONParser<*>.parseOfType(
	source: Reader
): Value? =
	doParseWithClass(source, valueClass = Value::class.java)


inline fun <reified Value : Any> JSONParser<*>.parseOfType(
	source: String
): Value? =
	doParseWithClass(source, valueClass = Value::class.java)
