package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONReader {

	val nextToken: JSONToken?

	fun readBoolean(): Boolean
	fun readBooleanOrNull(): Boolean?
	fun readDouble(): Double
	fun readDoubleOrNull(): Double?
	fun readFloat(): Float
	fun readFloatOrNull(): Float
	fun readInt(): Int
	fun readIntOrNull(): Int?
	fun readList(): List<Any?>
	fun readListEnd()
	fun readListStart()
	fun readLong(): Long
	fun readLongOrNUll(): Long?
	fun readMap(): Map<String, Any?>
	fun readMapEnd()
	fun readMapStart()
	fun readNull(): Nothing?
	fun readNumber(): Number
	fun readNumberOrNull(): Number?
	fun readString(): String
	fun readStringOrNull(): String?
	fun skipValue()
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readList(read: Reader.() -> Value): Value {
	readListStart()
	val result = read()
	readListEnd()

	return result
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader, Value> Reader.readMap(read: Reader.() -> Value): Value {
	readMapStart()
	val result = read()
	readMapEnd()

	return result
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Reader : JSONReader> Reader.readMapByEntry(readEntry: Reader.(name: String) -> Unit) {
	readMap {
		while (nextToken != JSONToken.objectEnd) {
			readEntry(readString())
		}
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONReader.readDecodable(): Any? {
	val token = nextToken
	return when (token) {
		JSONToken.booleanValue -> readBoolean()
		JSONToken.arrayStart -> readList()
		JSONToken.nullValue -> readNull()
		JSONToken.numberValue -> readNumber()
		JSONToken.objectStart -> readMap()
		JSONToken.stringValue -> readString()
		else -> throw JSONException("Cannot read value. Current token is $token")
	}
}
