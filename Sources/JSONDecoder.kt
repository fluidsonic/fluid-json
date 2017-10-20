package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONDecoder<out Context : JSONCoderContext> : JSONReader {

	val context: Context

	fun <Value : Any> readDecodableOfClass(valueClass: Class<in Value>): Value


	companion object {

		operator fun invoke(
			source: String,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONDecoder<JSONCoderContext> =
			StandardDecoder(codecResolver = codecResolver, context = JSONCoderContext.empty, source = JSONReader(source))


		operator fun invoke(
			source: Reader,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONDecoder<JSONCoderContext> =
			StandardDecoder(codecResolver = codecResolver, context = JSONCoderContext.empty, source = JSONReader(source))


		operator fun invoke(
			source: JSONReader,
			codecResolver: JSONCodecResolver<JSONCoderContext>
		): JSONDecoder<JSONCoderContext> =
			StandardDecoder(codecResolver = codecResolver, context = JSONCoderContext.empty, source = source)


		operator fun <Context : JSONCoderContext> invoke(
			source: String,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONDecoder<Context> =
			StandardDecoder(codecResolver = codecResolver, context = context, source = JSONReader(source))


		operator fun <Context : JSONCoderContext> invoke(
			source: Reader,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONDecoder<Context> =
			StandardDecoder(codecResolver = codecResolver, context = context, source = JSONReader(source))


		operator fun <Context : JSONCoderContext> invoke(
			source: JSONReader,
			context: Context,
			codecResolver: JSONCodecResolver<Context>
		): JSONDecoder<Context> =
			StandardDecoder(codecResolver = codecResolver, context = context, source = source)
	}
}


inline fun <reified Value : Any> JSONDecoder<*>.readDecodable() =
	readDecodableOfClass(Value::class.java)


inline fun <reified Value : Any> JSONDecoder<*>.readDecodableOrNull() =
	if (nextToken != JSONToken.nullValue) readDecodable<Value>() else readNull()


inline fun <reified Element : Any> JSONDecoder<*>.readListOfDecodableElements() =
	readListByElement { readDecodableOfClass(Element::class.java) }


inline fun <reified ElementKey : Any, reified ElementValue : Any> JSONDecoder<*>.readMapOfDecodableElements() =
	readMapByElement { readDecodableOfClass(ElementKey::class.java) to readDecodableOfClass(ElementValue::class.java) }


inline fun <reified ElementValue : Any> JSONDecoder<*>.readMapOfDecodableValues() =
	readMapByElement { readMapKey() to readDecodableOfClass(ElementValue::class.java) }


inline fun <reified ElementKey : Any> JSONDecoder<*>.readMapOfDecodableKeys() =
	readMapByElement { readDecodableOfClass(ElementKey::class.java) to readValue() }


inline fun <reified Element : Any> JSONDecoder<*>.readListOrNullOfDecodableElements() =
	if (nextToken != JSONToken.nullValue) readListOfDecodableElements<Element>() else readNull()


inline fun <reified ElementKey : Any, reified ElementValue : Any> JSONDecoder<*>.readMapOrNullOfDecodableElements() =
	if (nextToken != JSONToken.nullValue) readMapOfDecodableElements<ElementKey, ElementValue>() else readNull()


inline fun <reified ElementKey : Any> JSONDecoder<*>.readMapOrNullOfDecodableKeys() =
	if (nextToken != JSONToken.nullValue) readMapOfDecodableKeys<ElementKey>() else readNull()


inline fun <reified ElementValue : Any> JSONDecoder<*>.readMapOrNullOfDecodableValues() =
	if (nextToken != JSONToken.nullValue) readMapOfDecodableValues<ElementValue>() else readNull()
