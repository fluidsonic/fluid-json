package com.github.fluidsonic.fluid.json

import java.io.*

// TODO re-enable contracts once fixed:
// - https://youtrack.jetbrains.com/issue/KT-29510
// - https://youtrack.jetbrains.com/issue/KT-29614


interface JSONReader : Closeable {

	val depth: JSONDepth
	val isInValueIsolation: Boolean
	val nextToken: JSONToken?
	val offset: Int
	val path: JSONPath

	fun beginValueIsolation(): JSONDepth
	fun endValueIsolation(depth: JSONDepth)
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
		if (value.length != 1) throw JSONException.Schema(
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


	fun readValue(): Any {
		val token = nextToken
		return when (token) {
			JSONToken.booleanValue -> readBoolean()
			JSONToken.listStart -> readList()
			JSONToken.mapKey -> readMapKey()
			JSONToken.mapStart -> readMap()
			JSONToken.numberValue -> readNumber()
			JSONToken.stringValue -> readString()
			else -> throw JSONException.Parsing(
				message = "Cannot read value. Current token is '$token'",
				offset = offset,
				path = path
			)
		}
	}


	fun skipValue() {
		val token = nextToken
		when (token) {
			JSONToken.booleanValue -> readBoolean()
			JSONToken.listStart -> readListByElement { skipValue() }
			JSONToken.mapKey -> readMapKey()
			JSONToken.mapStart -> readMapByElementValue { skipValue() }
			JSONToken.nullValue -> readNull()
			JSONToken.numberValue -> readNumber()
			JSONToken.stringValue -> readString()
			else -> throw JSONException.Parsing(
				message = "Cannot skip value if next token is '$token'",
				offset = offset,
				path = path
			)
		}
	}


	companion object {

		fun build(source: Reader): JSONReader =
			StandardReader(TextInput(source))


		fun build(source: String) =
			build(StringReader(source))
	}
}


inline fun <Reader : JSONReader, Value> Reader.isolateValueRead(crossinline read: Reader.() -> Value): Value {
	val depth = beginValueIsolation()
	val value = read()
	endValueIsolation(depth = depth)

	return value
}


fun JSONReader.readBooleanOrNull() =
	if (nextToken != JSONToken.nullValue) readBoolean() else readNull()


fun JSONReader.readByteOrNull() =
	if (nextToken != JSONToken.nullValue) readByte() else readNull()


fun JSONReader.readCharOrNull() =
	if (nextToken != JSONToken.nullValue) readChar() else readNull()


fun JSONReader.readDoubleOrNull() =
	if (nextToken != JSONToken.nullValue) readDouble() else readNull()


fun JSONReader.readFloatOrNull() =
	if (nextToken != JSONToken.nullValue) readFloat() else readNull()


fun JSONReader.readIntOrNull() =
	if (nextToken != JSONToken.nullValue) readInt() else readNull()


inline fun <Reader : JSONReader, Value> Reader.readFromList(crossinline readContent: Reader.() -> Value): Value {
//	contract {
//		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
//	}

	return isolateValueRead {
		readListStart()
		readContent().also { readListEnd() }
	}
}


inline fun <Reader : JSONReader, Value> Reader.readFromMap(crossinline readContent: Reader.() -> Value): Value {
//	contract {
//		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
//	}

	return isolateValueRead {
		readMapStart()
		readContent().also { readMapEnd() }
	}
}


inline fun <Reader : JSONReader> Reader.readFromListByElement(crossinline readElement: Reader.() -> Unit) {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	readFromList {
		while (nextToken != JSONToken.listEnd) {
			isolateValueRead(readElement)
		}
	}
}


inline fun <Reader : JSONReader> Reader.readFromMapByElement(crossinline readElement: Reader.() -> Unit) {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	readFromMap {
		while (nextToken != JSONToken.mapEnd)
			readElement()
	}
}


inline fun <Reader : JSONReader> Reader.readFromMapByElementValue(crossinline readElementValue: Reader.(key: String) -> Unit) {
//	contract {
//		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
//	}

	readFromMapByElement {
		val key = readMapKey()
		isolateValueRead { readElementValue(key) }
	}
}


fun JSONReader.readList() =
	readListByElement { readValueOrNull() }


inline fun <Reader : JSONReader, Value> Reader.readListByElement(crossinline readElement: Reader.() -> Value): List<Value> {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return mutableListOf<Value>().also { list ->
		readFromListByElement {
			list += readElement()
		}
	}
}


fun JSONReader.readListOrNull() =
	readOrNull { readList() }


inline fun <Reader : JSONReader, Value> Reader.readListOrNullByElement(crossinline readElement: Reader.() -> Value): List<Value>? {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readListByElement(readElement) }
}


fun JSONReader.readLongOrNull() =
	readOrNull { readLong() }


fun JSONReader.readMap() =
	readMapByElementValue { readValueOrNull() }


inline fun <Reader : JSONReader, ElementKey, ElementValue> Reader.readMapByElement(
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


inline fun <Reader : JSONReader, ElementValue> Reader.readMapByElementValue(
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


fun JSONReader.readMapOrNull() =
	readOrNull { readMap() }


inline fun <Reader : JSONReader, ElementKey, ElementValue> Reader.readMapOrNullByElement(
	crossinline readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue>? {
//	contract {
//		callsInPlace(readElement, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readMapByElement(readElement) }
}


inline fun <Reader : JSONReader, ElementValue> Reader.readMapOrNullByElementValue(
	crossinline readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue>? {
//	contract {
//		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
//	}

	return readOrNull { readMapByElementValue(readElementValue) }
}


fun JSONReader.readNumberOrNull() =
	readOrNull { readNumber() }


inline fun <Reader : JSONReader, Value : Any> Reader.readOrNull(crossinline read: Reader.() -> Value): Value? {
//	contract {
//		callsInPlace(read, InvocationKind.AT_MOST_ONCE)
//	}

	return if (nextToken != JSONToken.nullValue)
		isolateValueRead(read)
	else
		readNull()
}


fun JSONReader.readShortOrNull() =
	readOrNull { readShort() }


fun JSONReader.readStringOrNull() =
	readOrNull { readString() }


fun JSONReader.readValueOrNull() =
	readOrNull { readValue() }


inline fun <Reader : JSONReader?, Result> Reader.use(withTermination: Boolean = true, block: (Reader) -> Result): Result {
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


inline fun <Reader : JSONReader, Result> Reader.withTermination(withTermination: Boolean = true, block: Reader.() -> Result): Result {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}
