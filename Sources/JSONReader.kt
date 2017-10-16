package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONReader {

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

	fun readBooleanOrNull() =
		if (nextToken != JSONToken.nullValue) readBoolean() else readNull()

	fun readDoubleOrNull() =
		if (nextToken != JSONToken.nullValue) readDouble() else readNull()

	fun readFloat() =
		readDouble().toFloat()

	fun readFloatOrNull() =
		if (nextToken != JSONToken.nullValue) readFloat() else readNull()

	fun readInt(): Int {
		val value = readLong()
		return when {
			value >= Int.MAX_VALUE -> Int.MAX_VALUE
			value <= Int.MIN_VALUE -> Int.MIN_VALUE
			else -> value.toInt()
		}
	}

	fun readIntOrNull() =
		if (nextToken != JSONToken.nullValue) readInt() else readNull()

	fun readList(): List<Any?> {
		val list = mutableListOf<Any?>()
		readListByElement { list += readValue() }
		return list
	}

	fun readListOrNull() =
		if (nextToken != JSONToken.nullValue) readList() else readNull()

	fun readLongOrNull() =
		if (nextToken != JSONToken.nullValue) readLong() else readNull()

	fun readMap(): Map<String, Any?> {
		val map = mutableMapOf<String, Any?>()
		readMapByEntry { key -> map[key] = readValue() }
		return map
	}

	fun readMapKey() =
		readString()

	fun readMapOrNull() =
		if (nextToken != JSONToken.nullValue) readMap() else readNull()

	fun readNumberOrNull() =
		if (nextToken != JSONToken.nullValue) readNumber() else readNull()

	fun readStringOrNull() =
		if (nextToken != JSONToken.nullValue) readString() else readNull()

	fun skipValue() {
		val token = nextToken
		when (token) {
			JSONToken.booleanValue -> readBoolean()
			JSONToken.listStart -> readListByElement { skipValue() }
			JSONToken.mapKey -> readMapKey()
			JSONToken.mapStart -> readMapByEntry { skipValue() }
			JSONToken.nullValue -> readNull()
			JSONToken.numberValue -> readNumber()
			JSONToken.stringValue -> readString()
			else -> throw JSONException("Cannot skip value if next token is '$token'")
		}
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readList(read: Reader.() -> Value): Value { // FIXME is api confusing?
	readListStart()
	val result = read()
	readListEnd()

	return result
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader> Reader.readListByElement(readElement: Reader.() -> Unit) {// FIXME is api confusing?
	readList {
		while (nextToken != JSONToken.listEnd) {
			readElement()
		}
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readListOrNull(read: Reader.() -> Value) =
	if (nextToken != JSONToken.nullValue) readList(read) else readNull()


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readMap(read: Reader.() -> Value): Value {// FIXME is api confusing?
	readMapStart()
	val result = read()
	readMapEnd()

	return result
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readMapOrNull(read: Reader.() -> Value) =
	if (nextToken != JSONToken.nullValue) readMap(read) else readNull()


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader> Reader.readMapByEntry(readEntry: Reader.(key: String) -> Unit) {// FIXME is api confusing?
	readMap {
		while (nextToken != JSONToken.mapEnd) {
			readEntry(readMapKey())
		}
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONReader.readValue(): Any? {
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
