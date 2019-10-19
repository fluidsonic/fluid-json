package io.fluidsonic.json

import java.io.*

// TODO re-enable contracts once fixed:
// - https://youtrack.jetbrains.com/issue/KT-29510
// - https://youtrack.jetbrains.com/issue/KT-29614


interface JsonWriter : Closeable, Flushable {

	val depth: JsonDepth
	val isErrored: Boolean
	val isInValueIsolation: Boolean
	val path: JsonPath

	fun beginValueIsolation(): JsonDepth
	fun endValueIsolation(depth: JsonDepth)
	fun markAsErrored()
	fun terminate()
	fun writeBoolean(value: Boolean)
	fun writeDouble(value: Double)
	fun writeListEnd()
	fun writeListStart()
	fun writeLong(value: Long)
	fun writeMapEnd()
	fun writeMapStart()
	fun writeNull()
	fun writeString(value: String)


	fun writeByte(value: Byte) =
		writeLong(value.toLong())


	fun writeChar(value: Char) =
		writeString(value.toString())


	fun writeFloat(value: Float) =
		writeDouble(value.toDouble())


	fun writeInt(value: Int) =
		writeLong(value.toLong())


	fun writeMapKey(value: String) =
		writeString(value)


	fun writeNumber(value: Number) =
		when (value) {
			is Byte -> writeByte(value)
			is Float -> writeFloat(value)
			is Int -> writeInt(value)
			is Long -> writeLong(value)
			is Short -> writeShort(value)
			else -> writeDouble(value.toDouble())
		}


	fun writeShort(value: Short) =
		writeLong(value.toLong())


	fun writeValue(value: Any) {
		when (value) {
			is Array<*> -> writeList(value)
			is Boolean -> writeBoolean(value)
			is BooleanArray -> writeList(value)
			is Byte -> writeByte(value)
			is ByteArray -> writeList(value)
			is Char -> writeChar(value)
			is CharArray -> writeList(value)
			is DoubleArray -> writeList(value)
			is Float -> writeFloat(value)
			is FloatArray -> writeList(value)
			is Int -> writeInt(value)
			is IntArray -> writeList(value)
			is Iterable<*> -> writeList(value)
			is Long -> writeLong(value)
			is LongArray -> writeList(value)
			is Map<*, *> -> writeMap(value)
			is Sequence<*> -> writeList(value)
			is Short -> writeShort(value)
			is ShortArray -> writeList(value)
			is String -> writeString(value)
			is Number -> writeNumber(value) // after subclasses
			else -> throw JsonException.Serialization(
				message = "Cannot write JSON value of ${value::class}: $value",
				path = path
			)
		}
	}


	companion object {

		fun build(destination: Writer): JsonWriter =
			StandardWriter(destination)
	}
}


inline fun <Writer : JsonWriter> Writer.isolateValueWrite(crossinline write: Writer.() -> Unit) {
	val depth = beginValueIsolation()
	val value = write()
	endValueIsolation(depth = depth)

	return value
}


inline fun <Writer : JsonWriter?, Result> Writer.use(withTermination: Boolean = true, block: (Writer) -> Result): Result {
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


inline fun <Writer : JsonWriter, Result> Writer.withTermination(withTermination: Boolean = true, block: Writer.() -> Result): Result {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}


inline fun <Writer : JsonWriter, ReturnValue> Writer.withErrorChecking(block: Writer.() -> ReturnValue): ReturnValue {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	if (isErrored) {
		throw JsonException.Serialization(
			message = "Cannot operate on an errored JsonWriter",
			path = path
		)
	}

	return try {
		block()
	}
	catch (e: Throwable) {
		markAsErrored()
		throw e
	}
}


fun JsonWriter.writeBooleanOrNull(value: Boolean?) =
	writeOrNull(value, JsonWriter::writeBoolean)


fun JsonWriter.writeByteOrNull(value: Byte?) =
	writeOrNull(value, JsonWriter::writeByte)


fun JsonWriter.writeCharOrNull(value: Char?) =
	writeOrNull(value, JsonWriter::writeChar)


fun JsonWriter.writeDoubleOrNull(value: Double?) =
	writeOrNull(value, JsonWriter::writeDouble)


fun JsonWriter.writeFloatOrNull(value: Float?) =
	writeOrNull(value, JsonWriter::writeFloat)


fun JsonWriter.writeIntOrNull(value: Int?) =
	writeOrNull(value, JsonWriter::writeInt)


inline fun <Writer : JsonWriter> Writer.writeIntoList(crossinline writeContent: Writer.() -> Unit) {
//	contract {
//		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
//	}

	isolateValueWrite {
		writeListStart()
		writeContent()
		writeListEnd()
	}
}


inline fun <Writer : JsonWriter> Writer.writeIntoMap(crossinline writeContent: Writer.() -> Unit) {
//	contract {
//		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
//	}

	isolateValueWrite {
		writeMapStart()
		writeContent()
		writeMapEnd()
	}
}


fun JsonWriter.writeList(value: BooleanArray) =
	writeListByElement(value) { writeBoolean(it) }


fun JsonWriter.writeList(value: ByteArray) =
	writeListByElement(value) { writeByte(it) }


fun JsonWriter.writeList(value: CharArray) =
	writeListByElement(value) { writeChar(it) }


fun JsonWriter.writeList(value: DoubleArray) =
	writeListByElement(value) { writeDouble(it) }


fun JsonWriter.writeList(value: FloatArray) =
	writeListByElement(value) { writeFloat(it) }


fun JsonWriter.writeList(value: IntArray) =
	writeListByElement(value) { writeInt(it) }


fun JsonWriter.writeList(value: LongArray) =
	writeListByElement(value) { writeLong(it) }


fun JsonWriter.writeList(value: ShortArray) =
	writeListByElement(value) { writeShort(it) }


fun JsonWriter.writeList(value: Array<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


fun JsonWriter.writeList(value: Iterable<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


fun JsonWriter.writeList(value: Sequence<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: BooleanArray,
	crossinline writeElement: Writer.(element: Boolean) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: ByteArray,
	crossinline writeElement: Writer.(element: Byte) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: CharArray,
	crossinline writeElement: Writer.(element: Char) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: DoubleArray,
	crossinline writeElement: Writer.(element: Double) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: FloatArray,
	crossinline writeElement: Writer.(element: Float) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: IntArray,
	crossinline writeElement: Writer.(element: Int) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: LongArray,
	crossinline writeElement: Writer.(element: Long) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: ShortArray,
	crossinline writeElement: Writer.(element: Short) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Array<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Iterable<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Sequence<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


fun JsonWriter.writeListOrNull(value: BooleanArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: ByteArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: CharArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: DoubleArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: FloatArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: IntArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: LongArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: ShortArray?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: Array<*>?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: Iterable<*>?) =
	writeOrNull(value, JsonWriter::writeList)


fun JsonWriter.writeListOrNull(value: Sequence<*>?) =
	writeOrNull(value, JsonWriter::writeList)


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: BooleanArray?,
	crossinline writeElement: Writer.(element: Boolean) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ByteArray?,
	crossinline writeElement: Writer.(element: Byte) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: CharArray?,
	crossinline writeElement: Writer.(element: Char) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: DoubleArray?,
	crossinline writeElement: Writer.(element: Double) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: FloatArray?,
	crossinline writeElement: Writer.(element: Float) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: IntArray?,
	crossinline writeElement: Writer.(element: Int) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: LongArray?,
	crossinline writeElement: Writer.(element: Long) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ShortArray?,
	crossinline writeElement: Writer.(element: Short) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Array<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Iterable<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Sequence<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


fun JsonWriter.writeLongOrNull(value: Long?) =
	writeOrNull(value, JsonWriter::writeLong)


fun JsonWriter.writeMap(value: Map<*, *>) =
	writeMapByElementValue(value) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter, ElementKey, ElementValue> Writer.writeMapByElement(
	value: Map<ElementKey, ElementValue>,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeIntoMap {
		for ((elementKey, elementValue) in value)
			writeElement(elementKey, elementValue)
	}
}


inline fun <Writer : JsonWriter, ElementValue> Writer.writeMapByElementValue(
	value: Map<*, ElementValue>,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
//	}

	writeMapByElement(value) { elementKey, elementValue ->
		writeValueOrNull(elementKey)
		isolateValueWrite {
			writeElementValue(elementValue)
		}
	}
}


fun JsonWriter.writeMapOrNull(value: Map<*, *>?) =
	writeOrNull(value, JsonWriter::writeMap)


inline fun <Writer : JsonWriter, ElementKey, ElementValue> Writer.writeMapOrNullByElement(
	value: Map<ElementKey, ElementValue>?,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeMapByElement(it, writeElement) }
}


inline fun <Writer : JsonWriter, ElementValue> Writer.writeMapOrNullByElementValue(
	value: Map<*, ElementValue>?,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeMapByElementValue(it, writeElementValue) }
}


fun JsonWriter.writeMapElement(key: String, boolean: Boolean) {
	writeMapKey(key)
	writeBoolean(boolean)
}


fun JsonWriter.writeMapElement(key: String, boolean: Boolean?, skipIfNull: Boolean = false) =
	if (boolean != null)
		writeMapElement(key, boolean)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, byte: Byte) {
	writeMapKey(key)
	writeByte(byte)
}


fun JsonWriter.writeMapElement(key: String, byte: Byte?, skipIfNull: Boolean = false) =
	if (byte != null)
		writeMapElement(key, byte)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, char: Char) {
	writeMapKey(key)
	writeChar(char)
}


fun JsonWriter.writeMapElement(key: String, char: Char?, skipIfNull: Boolean = false) =
	if (char != null)
		writeMapElement(key, char)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, double: Double) {
	writeMapKey(key)
	writeDouble(double)
}


fun JsonWriter.writeMapElement(key: String, double: Double?, skipIfNull: Boolean = false) =
	if (double != null)
		writeMapElement(key, double)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, float: Float) {
	writeMapKey(key)
	writeFloat(float)
}


fun JsonWriter.writeMapElement(key: String, float: Float?, skipIfNull: Boolean = false) =
	if (float != null)
		writeMapElement(key, float)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, int: Int) {
	writeMapKey(key)
	writeInt(int)
}


fun JsonWriter.writeMapElement(key: String, int: Int?, skipIfNull: Boolean = false) =
	if (int != null)
		writeMapElement(key, int)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: Array<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Array<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


fun JsonWriter.writeMapElement(key: String, list: BooleanArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: ByteArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: CharArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: DoubleArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: FloatArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: IntArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: Iterable<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Iterable<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


fun JsonWriter.writeMapElement(key: String, list: LongArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, list: Sequence<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Sequence<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


fun JsonWriter.writeMapElement(key: String, list: ShortArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, long: Long) {
	writeMapKey(key)
	writeLong(long)
}


fun JsonWriter.writeMapElement(key: String, long: Long?, skipIfNull: Boolean = false) =
	if (long != null)
		writeMapElement(key, long)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, map: Map<*, *>?, skipIfNull: Boolean = false) =
	writeMapElement(key, map = map, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JsonWriter, Child> Writer.writeMapElement(
	key: String,
	map: Map<*, Child>?,
	skipIfNull: Boolean = false,
	crossinline writeChild: Writer.(value: Child) -> Unit
) =
	if (map != null) {
		writeMapKey(key)
		writeMapByElementValue(map, writeChild)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, number: Number?, skipIfNull: Boolean = false) =
	if (number != null) {
		writeMapKey(key)
		writeNumber(number)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, short: Short) {
	writeMapKey(key)
	writeShort(short)
}


fun JsonWriter.writeMapElement(key: String, short: Short?, skipIfNull: Boolean = false) =
	if (short != null)
		writeMapElement(key, short)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, string: String?, skipIfNull: Boolean = false) =
	if (string != null) {
		writeMapKey(key)
		writeString(string)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JsonWriter.writeMapElement(key: String, value: Any?, skipIfNull: Boolean = false) =
	writeMapElement(key, value, skipIfNull) { writeValue(it) }


inline fun <Writer : JsonWriter, Value : Any> Writer.writeMapElement(
	key: String,
	value: Value?,
	skipIfNull: Boolean = false,
	crossinline writeCustomValue: Writer.(value: Value) -> Unit
) {
//	contract {
//		callsInPlace(writeCustomValue, InvocationKind.AT_MOST_ONCE)
//	}

	if (value != null) {
		writeMapKey(key)
		isolateValueWrite {
			writeCustomValue(value)
		}
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


inline fun <Writer : JsonWriter> Writer.writeMapElement(
	key: String,
	crossinline writeValue: Writer.() -> Unit
) {
//	contract {
//		callsInPlace(writeValue, InvocationKind.EXACTLY_ONCE)
//	}

	writeMapKey(key)
	isolateValueWrite {
		writeValue()
	}
}


fun JsonWriter.writeMapNullElement(key: String) {
	writeMapKey(key)
	writeNull()
}


fun JsonWriter.writeNumberOrNull(value: Number?) =
	writeOrNull(value, JsonWriter::writeNumber)


inline fun <Writer : JsonWriter, Value : Any> Writer.writeOrNull(value: Value?, crossinline write: Writer.(value: Value) -> Unit) {
//	contract {
//		callsInPlace(write, InvocationKind.AT_MOST_ONCE)
//	}

	if (value != null)
		isolateValueWrite { write(value) }
	else
		writeNull()
}


fun JsonWriter.writeShortOrNull(value: Short?) =
	writeOrNull(value, JsonWriter::writeShort)


fun JsonWriter.writeStringOrNull(value: String?) =
	writeOrNull(value, JsonWriter::writeString)


fun JsonWriter.writeValueOrNull(value: Any?) =
	writeOrNull(value, JsonWriter::writeValue)
