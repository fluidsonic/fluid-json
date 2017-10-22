package com.github.fluidsonic.fluid.json

import java.io.Reader


internal class StandardParser<Context : JSONCoderContext>(
	override val context: Context,
	private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<in Context>
) : JSONParser<Context> {

	override fun <Value : Any> doParseWithClass(
		source: Reader,
		valueClass: Class<out Value>
	): Value? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readDecodableOrNullOfClass(valueClass)
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>
	): List<Value>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readListByElement { readDecodableOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Value : Any> doParseListWithClass(
		source: Reader,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Value
	): List<Value?>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readListByElement { readDecodableOrNullOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>
	): Map<Key, Value>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readMapByElement { readDecodableOfClass(keyClass) to readDecodableOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Key
	): Map<Key?, Value>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readMapByElement { readDecodableOrNullOfClass(keyClass) to readDecodableOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.KeyAndValue
	): Map<Key?, Value?>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readMapByElement { readDecodableOrNullOfClass(keyClass) to readDecodableOrNullOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <Key : Any, Value : Any> doParseMapWithClasses(
		source: Reader,
		keyClass: Class<out Key>,
		valueClass: Class<out Value>,
		nullability: JSONNullability.Value
	): Map<Key, Value?>? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readMapByElement { readDecodableOfClass(keyClass) to readDecodableOrNullOfClass(valueClass) }
			decoder.readEndOfInput()

			value
		}
	}


	override fun <NewContext : Context> withContext(context: NewContext) =
		StandardParser(context = context, decoderFactory = decoderFactory)
}
