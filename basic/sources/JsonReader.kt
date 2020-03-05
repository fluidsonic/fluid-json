package io.fluidsonic.json

import java.io.*

// TODO re-enable contracts once fixed:
// - https://youtrack.jetbrains.com/issue/KT-29510
// - https://youtrack.jetbrains.com/issue/KT-29614


interface JsonReader : Closeable {

	val depth: JsonDepth
	val isInValueIsolation: Boolean
	val nextToken: JsonToken?
	val offset: Int
	val path: JsonPath

	fun beginValueIsolation(): JsonDepth
	fun endValueIsolation(depth: JsonDepth)
	fun readBoolean(): Boolean
	fun readDouble(): Double
	fun readListEnd()
	fun readListStart()
	fun readLong(): Long
	fun readMapEnd()
	fun readMapStart()
	fun readNull(): Nothing?
	fun readNumber(): Number
	fun readString(): String
	fun terminate()


	fun readByte(): Byte {
		val value = readLong()
		return when {
			value >= Byte.MAX_VALUE -> Byte.MAX_VALUE
			value <= Byte.MIN_VALUE -> Byte.MIN_VALUE
			else -> value.toByte()
		}
	}


	fun readChar(): Char {
		val value = readString()
		if (value.length != 1) throw JsonException.Schema(
			message = "Expected value to be a string of exactly one UTF-16 character",
			path = path
		)

		return value[0]
	}


	fun readFloat() =
		readDouble().toFloat()


	fun readInt(): Int {
		val value = readLong()
		return when {
			value >= Int.MAX_VALUE -> Int.MAX_VALUE
			value <= Int.MIN_VALUE -> Int.MIN_VALUE
			else -> value.toInt()
		}
	}


	fun readMapKey() =
		readString()


	fun readShort(): Short {
		val value = readLong()
		return when {
			value >= Short.MAX_VALUE -> Short.MAX_VALUE
			value <= Short.MIN_VALUE -> Short.MIN_VALUE
			else -> value.toShort()
		}
	}


	fun readValue(): Any =
		when (val token = nextToken) {
			JsonToken.booleanValue -> readBoolean()
			JsonToken.listStart -> readList()
			JsonToken.mapKey -> readMapKey()
			JsonToken.mapStart -> readMap()
			JsonToken.numberValue -> readNumber()
			JsonToken.stringValue -> readString()
			else -> throw JsonException.Parsing(
				message = "Cannot read value. Current token is '$token'",
				offset = offset,
				path = path
			)
		}


	fun skipValue() {
		when (val token = nextToken) {
			JsonToken.booleanValue -> readBoolean()
			JsonToken.listStart -> readListByElement { skipValue() }
			JsonToken.mapKey -> readMapKey()
			JsonToken.mapStart -> readMapByElementValue { skipValue() }
			JsonToken.nullValue -> readNull()
			JsonToken.numberValue -> readNumber()
			JsonToken.stringValue -> readString()
			else -> throw JsonException.Parsing(
				message = "Cannot skip value if next token is '$token'",
				offset = offset,
				path = path
			)
		}
	}


	companion object {

		fun build(source: Reader): JsonReader =
			StandardReader(TextInput(source))


		fun build(source: String) =
			build(StringReader(source))
	}
}


inline fun <Reader : JsonReader, Value> Reader.isolateValueRead(crossinline read: Reader.() -> Value): Value {
	val depth = beginValueIsolation()
	val value = read()
	endValueIsolation(depth = depth)

	return value
}


fun JsonReader.readBooleanOrNull() =
	if (nextToken != JsonToken.nullValue) readBoolean() else readNull()


fun JsonReader.readByteOrNull() =
	if (nextToken != JsonToken.nullValue) readByte() else readNull()


fun JsonReader.readCharOrNull() =
	if (nextToken != JsonToken.nullValue) readChar() else readNull()


fun JsonReader.readDoubleOrNull() =
	if (nextToken != JsonToken.nullValue) readDouble() else readNull()


fun JsonReader.readFloatOrNull() =
	if (nextToken != JsonToken.nullValue) readFloat() else readNull()


fun JsonReader.readIntOrNull() =
	if (nextToken != JsonToken.nullValue) readInt() else readNull()


inline fun <Reader : JsonReader, Value> Reader.readFromList(crossinline readContent: Reader.() -> Value): Value {
//	contract {
//		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
//	}

	return isolateValueRead {
		readListStart()
		readContent().also { readListEnd() }
	}
}


inline fun <Reader : JsonReader, Value> Reader.readFromMap(crossinline readContent: Reader.() -> Value): Value {
//	contract {
//		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
//	}

	return isolateValueRead {
		readMapStart()
		readContent().also { readMapEnd() }
	}
}


inline fun <Reader : JsonReader> Reader.readFromListByElement(crossinline readElement: Reader.() -> Unit) {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	readFromList {
		while (nextToken != JsonToken.listEnd) {
			isolateValueRead(readElement)
		}
	}
}


inline fun <Reader : JsonReader> Reader.readFromMapByElement(crossinline readElement: Reader.() -> Unit) {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	readFromMap {
		while (nextToken != JsonToken.mapEnd)
			readElement()
	}
}


inline fun <Reader : JsonReader> Reader.readFromMapByElementValue(crossinline readElementValue: Reader.(key: String) -> Unit) {
//	contract {
//		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
//	}

	readFromMapByElement {
		val key = readMapKey()
		isolateValueRead { readElementValue(key) }
	}
}


fun JsonReader.readList() =
	readListByElement { readValueOrNull() }


inline fun <Reader : JsonReader, Value> Reader.readListByElement(crossinline readElement: Reader.() -> Value): List<Value> {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return mutableListOf<Value>().also { list ->
		readFromListByElement {
			list += readElement()
		}
	}
}


fun JsonReader.readListOrNull() =
	readOrNull { readList() }


inline fun <Reader : JsonReader, Value> Reader.readListOrNullByElement(crossinline readElement: Reader.() -> Value): List<Value>? {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readListByElement(readElement) }
}


fun JsonReader.readLongOrNull() =
	readOrNull { readLong() }


fun JsonReader.readMap() =
	readMapByElementValue { readValueOrNull() }


inline fun <Reader : JsonReader, ElementKey, ElementValue> Reader.readMapByElement(
	crossinline readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue> {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return mutableMapOf<ElementKey, ElementValue>().also { map ->
		readFromMapByElement {
			map += readElement()
		}
	}
}


inline fun <Reader : JsonReader, ElementValue> Reader.readMapByElementValue(
	crossinline readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue> {
//	contract {
//		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
//	}

	return mutableMapOf<String, ElementValue>().also { map ->
		readFromMapByElementValue { key ->
			map[key] = readElementValue(key)
		}
	}
}


fun JsonReader.readMapOrNull() =
	readOrNull { readMap() }


inline fun <Reader : JsonReader, ElementKey, ElementValue> Reader.readMapOrNullByElement(
	crossinline readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue>? {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readMapByElement(readElement) }
}


inline fun <Reader : JsonReader, ElementValue> Reader.readMapOrNullByElementValue(
	crossinline readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue>? {
//	contract {
//		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readMapByElementValue(readElementValue) }
}


fun JsonReader.readNumberOrNull() =
	readOrNull { readNumber() }


inline fun <Reader : JsonReader, Value : Any> Reader.readOrNull(crossinline read: Reader.() -> Value): Value? {
//	contract {
//		callsInPlace(read, InvocationKind.AT_MOST_ONCE)
//	}

	return if (nextToken != JsonToken.nullValue)
		isolateValueRead(read)
	else
		readNull()
}


fun JsonReader.readShortOrNull() =
	readOrNull { readShort() }


fun JsonReader.readStringOrNull() =
	readOrNull { readString() }


fun JsonReader.readValueOrNull() =
	readOrNull { readValue() }


inline fun <Reader : JsonReader?, Result> Reader.use(withTermination: Boolean = true, block: (Reader) -> Result): Result {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	var exception: Throwable? = null
	try {
		return block(this)
	}
	catch (e: Throwable) {
		exception = e
		throw e
	}
	finally {
		val finalException = exception

		when {
			this == null ->
				Unit

			finalException == null ->
				if (withTermination)
					terminate()
				else
					close()

			else ->
				try {
					close()
				}
				catch (closeException: Throwable) {
					finalException.addSuppressed(closeException)
				}
		}
	}
}


inline fun <Reader : JsonReader, Result> Reader.withTermination(withTermination: Boolean = true, block: Reader.() -> Result): Result {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}
