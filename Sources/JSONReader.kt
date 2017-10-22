package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Reader
import java.io.StringReader


interface JSONReader : Closeable {

	val nextToken: JSONToken?

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


	fun readByte(): Byte {
		val value = readLong()
		return when {
			value >= Byte.MAX_VALUE -> Byte.MAX_VALUE
			value <= Byte.MIN_VALUE -> Byte.MIN_VALUE
			else -> value.toByte()
		}
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
			else -> throw JSONException("Cannot skip value if next token is '$token'")
		}
	}


	companion object {

		fun build(source: Reader): JSONReader =
			StandardReader(TextInput(source))


		fun build(source: String) =
			build(StringReader(source))
	}
}


fun JSONReader.readBooleanOrNull() =
	if (nextToken != JSONToken.nullValue) readBoolean() else readNull()


fun JSONReader.readByteOrNull() =
	if (nextToken != JSONToken.nullValue) readByte() else readNull()


fun JSONReader.readDoubleOrNull() =
	if (nextToken != JSONToken.nullValue) readDouble() else readNull()


inline fun <Reader : JSONReader> Reader.readElementsFromMap(readElement: Reader.(key: String) -> Unit) {
	readMapStart()
	while (nextToken != JSONToken.mapEnd)
		readElement(readMapKey())
	readMapEnd()
}


fun JSONReader.readEndOfInput() {
	val nextToken = nextToken
	if (nextToken != null) {
		throw JSONException("Expected end of input but found token $nextToken")
	}
}


fun JSONReader.readFloatOrNull() =
	if (nextToken != JSONToken.nullValue) readFloat() else readNull()


fun JSONReader.readIntOrNull() =
	if (nextToken != JSONToken.nullValue) readInt() else readNull()


inline fun <Reader : JSONReader, Value> Reader.readFromList(readContent: Reader.() -> Value): Value {
	readListStart()
	val value = readContent()
	readListEnd()

	return value
}


inline fun <Reader : JSONReader, Value> Reader.readFromMap(readContent: Reader.() -> Value): Value {
	readMapStart()
	val result = readContent()
	readMapEnd()

	return result
}


inline fun <Reader : JSONReader> Reader.readFromListByElement(
	readElement: Reader.() -> Unit
) =
	readFromList {
		while (nextToken != JSONToken.listEnd)
			readElement()
	}


inline fun <Reader : JSONReader> Reader.readFromMapByElement(
	readElement: Reader.() -> Unit
) =
	readFromMap {
		while (nextToken != JSONToken.mapEnd)
			readElement()
	}


inline fun <Reader : JSONReader> Reader.readFromMapByElementValue(
	readElementValue: Reader.(key: String) -> Unit
) =
	readFromMap {
		while (nextToken != JSONToken.mapEnd)
			readElementValue(readMapKey())
	}


fun JSONReader.readList(): List<Any> =
	readListByElement { readValue() ?: throw JSONException("unexpected null value in list") }


fun JSONReader.readList(@Suppress("UNUSED_PARAMETER") nullability: JSONNullability.Value): List<Any?> =
	readListByElement { readValue() }


inline fun <Reader : JSONReader, Value> Reader.readListByElement(readElement: Reader.() -> Value): List<Value> =
	mutableListOf<Value>().also { list ->
		readFromListByElement {
			list += readElement()
		}
	}


fun JSONReader.readListOrNull() =
	if (nextToken != JSONToken.nullValue) readList() else readNull()


fun JSONReader.readListOrNull(nullability: JSONNullability.Value) =
	if (nextToken != JSONToken.nullValue) readList(nullability) else readNull()


inline fun <Reader : JSONReader, Value> Reader.readListOrNullByElement(readElement: Reader.() -> Value): List<Value>? =
	if (nextToken != JSONToken.nullValue) readListByElement(readElement) else readNull()


fun JSONReader.readLongOrNull() =
	if (nextToken != JSONToken.nullValue) readLong() else readNull()


fun JSONReader.readMap() =
	readMapByElementValue { readValue() ?: throw JSONException("unexpected null value in list") }


fun JSONReader.readMap(@Suppress("UNUSED_PARAMETER") nullability: JSONNullability.Value) =
	readMapByElementValue { readValue() }


inline fun <Reader : JSONReader, ElementKey, ElementValue> Reader.readMapByElement(
	readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue> =
	mutableMapOf<ElementKey, ElementValue>().also { map ->
		readFromMapByElement {
			map += readElement()
		}
	}


inline fun <Reader : JSONReader, ElementValue> Reader.readMapByElementValue(
	readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue> =
	mutableMapOf<String, ElementValue>().also { map ->
		readFromMapByElementValue { key ->
			map[key] = readElementValue(key)
		}
	}


fun JSONReader.readMapOrNull() =
	if (nextToken != JSONToken.nullValue) readMap() else readNull()


fun JSONReader.readMapOrNull(@Suppress("UNUSED_PARAMETER") nullability: JSONNullability.Value) =
	if (nextToken != JSONToken.nullValue) readMap(nullability) else readNull()


inline fun <Reader : JSONReader, ElementKey, ElementValue> Reader.readMapOrNullByElement(
	readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue>? =
	if (nextToken != JSONToken.nullValue) readMapByElement(readElement) else readNull()


inline fun <Reader : JSONReader, ElementValue> Reader.readMapOrNullByElementValue(
	readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue>? =
	if (nextToken != JSONToken.nullValue) readMapByElementValue(readElementValue) else readNull()


fun JSONReader.readNumberOrNull() =
	if (nextToken != JSONToken.nullValue) readNumber() else readNull()


fun JSONReader.readShortOrNull() =
	if (nextToken != JSONToken.nullValue) readShort() else readNull()


fun JSONReader.readStringOrNull() =
	if (nextToken != JSONToken.nullValue) readString() else readNull()


fun JSONReader.readValue(): Any? {
	val token = nextToken
	return when (token) {
		JSONToken.booleanValue -> readBoolean()
		JSONToken.listStart -> readList()
		JSONToken.mapKey -> readMapKey()
		JSONToken.mapStart -> readMap()
		JSONToken.nullValue -> readNull()
		JSONToken.numberValue -> readNumber()
		JSONToken.stringValue -> readString()
		else -> throw JSONException("Cannot read value. Current token is '$token'")
	}
}
