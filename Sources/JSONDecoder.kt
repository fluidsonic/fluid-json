package com.github.fluidsonic.fluid.json


internal interface JSONDecoder<out Context : JSONCoderContext> : JSONReader {

	val context: Context


	fun <Value : Any> readDecodableOfClass(valueClass: Class<Value>): Value
}


internal inline fun <reified Value : Any> JSONDecoder<*>.readDecodable() =
	readDecodableOfClass(Value::class.java)


internal inline fun <reified Value : Any> JSONDecoder<*>.readDecodableOrNull(): Value? {
	if (nextToken == JSONToken.nullValue) {
		return null
	}

	return readDecodable()
}


internal inline fun <reified Value : Any> JSONDecoder<*>.readDecodables() = // FIXME easy to mess up with readDecodable()
	mutableListOf<Value>().also { list ->
		readListByElement { list += readDecodableOfClass(Value::class.java) } // FIXME unexpected that we cannot use readList {}
	}


internal inline fun <reified Value : Any> JSONDecoder<*>.readDecodablesOrNull(): List<Value>? {
	if (nextToken == JSONToken.nullValue) {
		return null
	}

	return readDecodables()
}
