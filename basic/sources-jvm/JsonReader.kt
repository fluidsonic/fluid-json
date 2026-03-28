package io.fluidsonic.json

import java.io.*
import kotlin.contracts.*


/**
 * Reads JSON tokens and values from a source in a streaming fashion.
 */
public interface JsonReader : Closeable {

	public val depth: JsonDepth
	public val isInValueIsolation: Boolean
	public val nextToken: JsonToken?
	public val offset: Int
	public val path: JsonPath

	public fun beginValueIsolation(): JsonDepth
	public fun endValueIsolation(depth: JsonDepth)
	public fun readBoolean(): Boolean
	public fun readDouble(): Double
	public fun readListEnd()
	public fun readListStart()
	public fun readLong(): Long
	public fun readMapEnd()
	public fun readMapStart()
	public fun readNull(): Nothing?
	public fun readNumber(): Number
	public fun readString(): String
	public fun terminate()


	public fun readByte(): Byte {
		val value = readLong()
		return when {
			value >= Byte.MAX_VALUE -> Byte.MAX_VALUE
			value <= Byte.MIN_VALUE -> Byte.MIN_VALUE
			else -> value.toByte()
		}
	}


	public fun readChar(): Char {
		val value = readString()
		if (value.length != 1) throw JsonException.Schema(
			message = "Expected value to be a string of exactly one UTF-16 character",
			path = path
		)

		return value[0]
	}


	public fun readFloat(): Float =
		readDouble().toFloat()


	public fun readInt(): Int {
		val value = readLong()
		return when {
			value >= Int.MAX_VALUE -> Int.MAX_VALUE
			value <= Int.MIN_VALUE -> Int.MIN_VALUE
			else -> value.toInt()
		}
	}


	public fun readMapKey(): String =
		readString()


	public fun readShort(): Short {
		val value = readLong()
		return when {
			value >= Short.MAX_VALUE -> Short.MAX_VALUE
			value <= Short.MIN_VALUE -> Short.MIN_VALUE
			else -> value.toShort()
		}
	}


	public fun readValue(): Any =
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


	public fun skipValue() {
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


	public companion object {

		public fun build(source: Reader): JsonReader =
			StandardReader(TextInput(source))


		public fun build(source: String): JsonReader =
			build(StringReader(source))
	}
}


/** Reads exactly one value in isolation, ensuring no additional tokens are consumed. */
public inline fun <Reader : JsonReader, Value> Reader.isolateValueRead(crossinline read: Reader.() -> Value): Value {
	contract {
		callsInPlace(read, InvocationKind.EXACTLY_ONCE)
	}

	val depth = beginValueIsolation()
	val value = read()
	endValueIsolation(depth = depth)

	return value
}


/** Reads a JSON boolean value, or `null` if the next token is a JSON null. */
public fun JsonReader.readBooleanOrNull(): Boolean? =
	if (nextToken != JsonToken.nullValue) readBoolean() else readNull()


/** Reads a JSON number as a [Byte], or `null` if the next token is a JSON null. */
public fun JsonReader.readByteOrNull(): Byte? =
	if (nextToken != JsonToken.nullValue) readByte() else readNull()


/** Reads a JSON string as a single [Char], or `null` if the next token is a JSON null. */
public fun JsonReader.readCharOrNull(): Char? =
	if (nextToken != JsonToken.nullValue) readChar() else readNull()


/** Reads a JSON number as a [Double], or `null` if the next token is a JSON null. */
public fun JsonReader.readDoubleOrNull(): Double? =
	if (nextToken != JsonToken.nullValue) readDouble() else readNull()


/** Reads a JSON number as a [Float], or `null` if the next token is a JSON null. */
public fun JsonReader.readFloatOrNull(): Float? =
	if (nextToken != JsonToken.nullValue) readFloat() else readNull()


/** Reads a JSON number as an [Int], or `null` if the next token is a JSON null. */
public fun JsonReader.readIntOrNull(): Int? =
	if (nextToken != JsonToken.nullValue) readInt() else readNull()


/** Reads a complete JSON list, returning the result of [readContent] which is invoked between list start and end tokens. */
public inline fun <Reader : JsonReader, Value> Reader.readFromList(crossinline readContent: Reader.() -> Value): Value {
	contract {
		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
	}

	return isolateValueRead {
		readListStart()
		readContent().also { readListEnd() }
	}
}


/** Reads a complete JSON map, returning the result of [readContent] which is invoked between map start and end tokens. */
public inline fun <Reader : JsonReader, Value> Reader.readFromMap(crossinline readContent: Reader.() -> Value): Value {
	contract {
		callsInPlace(readContent, InvocationKind.EXACTLY_ONCE)
	}

	return isolateValueRead {
		readMapStart()
		readContent().also { readMapEnd() }
	}
}


/** Reads a complete JSON list, invoking [readElement] for each element. */
public inline fun <Reader : JsonReader> Reader.readFromListByElement(crossinline readElement: Reader.() -> Unit) {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	readFromList {
		while (nextToken != JsonToken.listEnd) {
			isolateValueRead(readElement)
		}
	}
}


/** Reads a complete JSON map, invoking [readElement] for each key-value pair. */
public inline fun <Reader : JsonReader> Reader.readFromMapByElement(crossinline readElement: Reader.() -> Unit) {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	readFromMap {
		while (nextToken != JsonToken.mapEnd)
			readElement()
	}
}


/** Reads a complete JSON map, invoking [readElementValue] with each key for reading the corresponding value. */
public inline fun <Reader : JsonReader> Reader.readFromMapByElementValue(crossinline readElementValue: Reader.(key: String) -> Unit) {
	contract {
		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
	}

	readFromMapByElement {
		val key = readMapKey()
		isolateValueRead { readElementValue(key) }
	}
}


/** Reads a complete JSON list and returns it as a [List] of basic Kotlin types. */
public fun JsonReader.readList(): List<Any?> =
	readListByElement { readValueOrNull() }


/** Reads a complete JSON list, invoking [readElement] for each element and collecting results into a [List]. */
public inline fun <Reader : JsonReader, Value> Reader.readListByElement(crossinline readElement: Reader.() -> Value): List<Value> {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	return mutableListOf<Value>().also { list ->
		readFromListByElement {
			list += readElement()
		}
	}
}


/** Reads a complete JSON list, or `null` if the next token is a JSON null. */
public fun JsonReader.readListOrNull(): List<Any?>? =
	readOrNull { readList() }


/** Reads a complete JSON list using [readElement] for each element, or `null` if the next token is a JSON null. */
public inline fun <Reader : JsonReader, Value> Reader.readListOrNullByElement(crossinline readElement: Reader.() -> Value): List<Value>? {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	return readOrNull { readListByElement(readElement) }
}


/** Reads a JSON number as a [Long], or `null` if the next token is a JSON null. */
public fun JsonReader.readLongOrNull(): Long? =
	readOrNull { readLong() }


/** Reads a complete JSON map and returns it as a [Map] of basic Kotlin types. */
public fun JsonReader.readMap(): Map<String, Any?> =
	readMapByElementValue { readValueOrNull() }


/** Reads a complete JSON map, invoking [readElement] for each entry to produce key-value pairs. */
public inline fun <Reader : JsonReader, ElementKey, ElementValue> Reader.readMapByElement(
	crossinline readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue> {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	return mutableMapOf<ElementKey, ElementValue>().also { map ->
		readFromMapByElement {
			map += readElement()
		}
	}
}


/** Reads a complete JSON map, invoking [readElementValue] with each key to read its corresponding value. */
public inline fun <Reader : JsonReader, ElementValue> Reader.readMapByElementValue(
	crossinline readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue> {
	contract {
		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
	}

	return mutableMapOf<String, ElementValue>().also { map ->
		readFromMapByElementValue { key ->
			map[key] = readElementValue(key)
		}
	}
}


/** Reads a complete JSON map, or `null` if the next token is a JSON null. */
public fun JsonReader.readMapOrNull(): Map<String, Any?>? =
	readOrNull { readMap() }


/** Reads a complete JSON map using [readElement] for each entry, or `null` if the next token is a JSON null. */
public inline fun <Reader : JsonReader, ElementKey, ElementValue> Reader.readMapOrNullByElement(
	crossinline readElement: Reader.() -> Pair<ElementKey, ElementValue>
): Map<ElementKey, ElementValue>? {
	contract {
		callsInPlace(readElement, InvocationKind.UNKNOWN)
	}

	return readOrNull { readMapByElement(readElement) }
}


/** Reads a complete JSON map using [readElementValue] for each value, or `null` if the next token is a JSON null. */
public inline fun <Reader : JsonReader, ElementValue> Reader.readMapOrNullByElementValue(
	crossinline readElementValue: Reader.(key: String) -> ElementValue
): Map<String, ElementValue>? {
	contract {
		callsInPlace(readElementValue, InvocationKind.UNKNOWN)
	}

	return readOrNull { readMapByElementValue(readElementValue) }
}


/** Reads a JSON number as a [Number], or `null` if the next token is a JSON null. */
public fun JsonReader.readNumberOrNull(): Number? =
	readOrNull { readNumber() }


/** Reads a value using [read], or returns `null` if the next token is a JSON null. */
public inline fun <Reader : JsonReader, Value : Any> Reader.readOrNull(crossinline read: Reader.() -> Value): Value? {
	contract {
		callsInPlace(read, InvocationKind.AT_MOST_ONCE)
	}

	return if (nextToken != JsonToken.nullValue)
		isolateValueRead(read)
	else
		readNull()
}


/** Reads a JSON number as a [Short], or `null` if the next token is a JSON null. */
public fun JsonReader.readShortOrNull(): Short? =
	readOrNull { readShort() }


/** Reads a JSON string, or `null` if the next token is a JSON null. */
public fun JsonReader.readStringOrNull(): String? =
	readOrNull { readString() }


/** Reads any JSON value, or `null` if the next token is a JSON null. */
public fun JsonReader.readValueOrNull(): Any? =
	readOrNull { readValue() }


/** Executes [block] with this reader, then terminates or closes it depending on [withTermination]. */
public inline fun <Reader : JsonReader?, Result> Reader.use(withTermination: Boolean = true, block: (Reader) -> Result): Result {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

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


/** Executes [block] and optionally terminates this reader afterward based on [withTermination]. */
public inline fun <Reader : JsonReader, Result> Reader.withTermination(withTermination: Boolean = true, block: Reader.() -> Result): Result {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}
