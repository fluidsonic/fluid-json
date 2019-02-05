package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Flushable
import java.io.Writer

// FIXME re-enable contracts once fixed:
// - https://youtrack.jetbrains.com/issue/KT-29510
// - https://youtrack.jetbrains.com/issue/KT-29614


interface JSONWriter : Closeable, Flushable {

	val depth: JSONDepth
	val isErrored: Boolean
	val isInValueIsolation: Boolean
	val path: JSONPath

	fun beginValueIsolation(): JSONDepth
	fun endValueIsolation(depth: JSONDepth)
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
			else -> throw JSONException.Serialization(
				message = "Cannot write JSON value of ${value::class}: $value",
				path = path
			)
		}
	}


	companion object {

		fun build(destination: Writer): JSONWriter =
			StandardWriter(destination)
	}
}


inline fun <Writer : JSONWriter> Writer.isolateValueWrite(crossinline write: Writer.() -> Unit) {
	val depth = beginValueIsolation()
	val value = write()
	endValueIsolation(depth = depth)

	return value
}


inline fun <Writer : JSONWriter?, Result> Writer.use(withTermination: Boolean = true, block: (Writer) -> Result): Result {
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


inline fun <Writer : JSONWriter, Result> Writer.withTermination(withTermination: Boolean = true, block: Writer.() -> Result): Result {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}


inline fun <Writer : JSONWriter, ReturnValue> Writer.withErrorChecking(block: Writer.() -> ReturnValue): ReturnValue {
//	contract {
//		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//	}

	if (isErrored) {
		throw JSONException.Serialization(
			message = "Cannot operate on an errored JSONWriter",
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


fun JSONWriter.writeBooleanOrNull(value: Boolean?) =
	writeOrNull(value, JSONWriter::writeBoolean)


fun JSONWriter.writeByteOrNull(value: Byte?) =
	writeOrNull(value, JSONWriter::writeByte)


fun JSONWriter.writeCharOrNull(value: Char?) =
	writeOrNull(value, JSONWriter::writeChar)


fun JSONWriter.writeDoubleOrNull(value: Double?) =
	writeOrNull(value, JSONWriter::writeDouble)


fun JSONWriter.writeFloatOrNull(value: Float?) =
	writeOrNull(value, JSONWriter::writeFloat)


fun JSONWriter.writeIntOrNull(value: Int?) =
	writeOrNull(value, JSONWriter::writeInt)


inline fun <Writer : JSONWriter> Writer.writeIntoList(crossinline writeContent: Writer.() -> Unit) {
//	contract {
//		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
//	}

	isolateValueWrite {
		writeListStart()
		writeContent()
		writeListEnd()
	}
}


inline fun <Writer : JSONWriter> Writer.writeIntoMap(crossinline writeContent: Writer.() -> Unit) {
//	contract {
//		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
//	}

	isolateValueWrite {
		writeMapStart()
		writeContent()
		writeMapEnd()
	}
}


fun JSONWriter.writeList(value: BooleanArray) =
	writeListByElement(value) { writeBoolean(it) }


fun JSONWriter.writeList(value: ByteArray) =
	writeListByElement(value) { writeByte(it) }


fun JSONWriter.writeList(value: CharArray) =
	writeListByElement(value) { writeChar(it) }


fun JSONWriter.writeList(value: DoubleArray) =
	writeListByElement(value) { writeDouble(it) }


fun JSONWriter.writeList(value: FloatArray) =
	writeListByElement(value) { writeFloat(it) }


fun JSONWriter.writeList(value: IntArray) =
	writeListByElement(value) { writeInt(it) }


fun JSONWriter.writeList(value: LongArray) =
	writeListByElement(value) { writeLong(it) }


fun JSONWriter.writeList(value: ShortArray) =
	writeListByElement(value) { writeShort(it) }


fun JSONWriter.writeList(value: Array<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


fun JSONWriter.writeList(value: Iterable<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


fun JSONWriter.writeList(value: Sequence<*>) =
	writeListByElement(value) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
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


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
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


fun JSONWriter.writeListOrNull(value: BooleanArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: ByteArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: CharArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: DoubleArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: FloatArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: IntArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: LongArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: ShortArray?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: Array<*>?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: Iterable<*>?) =
	writeOrNull(value, JSONWriter::writeList)


fun JSONWriter.writeListOrNull(value: Sequence<*>?) =
	writeOrNull(value, JSONWriter::writeList)


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: BooleanArray?,
	crossinline writeElement: Writer.(element: Boolean) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: ByteArray?,
	crossinline writeElement: Writer.(element: Byte) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: CharArray?,
	crossinline writeElement: Writer.(element: Char) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: DoubleArray?,
	crossinline writeElement: Writer.(element: Double) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: FloatArray?,
	crossinline writeElement: Writer.(element: Float) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: IntArray?,
	crossinline writeElement: Writer.(element: Int) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: LongArray?,
	crossinline writeElement: Writer.(element: Long) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: ShortArray?,
	crossinline writeElement: Writer.(element: Short) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Array<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Iterable<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Sequence<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


fun JSONWriter.writeLongOrNull(value: Long?) =
	writeOrNull(value, JSONWriter::writeLong)


fun JSONWriter.writeMap(value: Map<*, *>) =
	writeMapByElementValue(value) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter, ElementKey, ElementValue> Writer.writeMapByElement(
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


inline fun <Writer : JSONWriter, ElementValue> Writer.writeMapByElementValue(
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


fun JSONWriter.writeMapOrNull(value: Map<*, *>?) =
	writeOrNull(value, JSONWriter::writeMap)


inline fun <Writer : JSONWriter, ElementKey, ElementValue> Writer.writeMapOrNullByElement(
	value: Map<ElementKey, ElementValue>?,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElement, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeMapByElement(it, writeElement) }
}


inline fun <Writer : JSONWriter, ElementValue> Writer.writeMapOrNullByElementValue(
	value: Map<*, ElementValue>?,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit
) {
//	contract {
//		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
//	}

	writeOrNull(value) { writeMapByElementValue(it, writeElementValue) }
}


fun JSONWriter.writeMapElement(key: String, boolean: Boolean) {
	writeMapKey(key)
	writeBoolean(boolean)
}


fun JSONWriter.writeMapElement(key: String, boolean: Boolean?, skipIfNull: Boolean = false) =
	if (boolean != null)
		writeMapElement(key, boolean)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, byte: Byte) {
	writeMapKey(key)
	writeByte(byte)
}


fun JSONWriter.writeMapElement(key: String, byte: Byte?, skipIfNull: Boolean = false) =
	if (byte != null)
		writeMapElement(key, byte)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, char: Char) {
	writeMapKey(key)
	writeChar(char)
}


fun JSONWriter.writeMapElement(key: String, char: Char?, skipIfNull: Boolean = false) =
	if (char != null)
		writeMapElement(key, char)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, double: Double) {
	writeMapKey(key)
	writeDouble(double)
}


fun JSONWriter.writeMapElement(key: String, double: Double?, skipIfNull: Boolean = false) =
	if (double != null)
		writeMapElement(key, double)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, float: Float) {
	writeMapKey(key)
	writeFloat(float)
}


fun JSONWriter.writeMapElement(key: String, float: Float?, skipIfNull: Boolean = false) =
	if (float != null)
		writeMapElement(key, float)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, int: Int) {
	writeMapKey(key)
	writeInt(int)
}


fun JSONWriter.writeMapElement(key: String, int: Int?, skipIfNull: Boolean = false) =
	if (int != null)
		writeMapElement(key, int)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: Array<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter, Element> Writer.writeMapElement(
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


fun JSONWriter.writeMapElement(key: String, list: BooleanArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: ByteArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: CharArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: DoubleArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: FloatArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: IntArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: Iterable<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter, Element> Writer.writeMapElement(
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


fun JSONWriter.writeMapElement(key: String, list: LongArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, list: Sequence<*>?, skipIfNull: Boolean = false) =
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter, Element> Writer.writeMapElement(
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


fun JSONWriter.writeMapElement(key: String, list: ShortArray?, skipIfNull: Boolean = false) =
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, long: Long) {
	writeMapKey(key)
	writeLong(long)
}


fun JSONWriter.writeMapElement(key: String, long: Long?, skipIfNull: Boolean = false) =
	if (long != null)
		writeMapElement(key, long)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, map: Map<*, *>?, skipIfNull: Boolean = false) =
	writeMapElement(key, map = map, skipIfNull = skipIfNull) { writeValueOrNull(it) }


inline fun <Writer : JSONWriter, Child> Writer.writeMapElement(
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


fun JSONWriter.writeMapElement(key: String, number: Number?, skipIfNull: Boolean = false) =
	if (number != null) {
		writeMapKey(key)
		writeNumber(number)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, short: Short) {
	writeMapKey(key)
	writeShort(short)
}


fun JSONWriter.writeMapElement(key: String, short: Short?, skipIfNull: Boolean = false) =
	if (short != null)
		writeMapElement(key, short)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, string: String?, skipIfNull: Boolean = false) =
	if (string != null) {
		writeMapKey(key)
		writeString(string)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapElement(key: String, value: Any?, skipIfNull: Boolean = false) =
	writeMapElement(key, value, skipIfNull) { writeValue(it) }


inline fun <Writer : JSONWriter, Value : Any> Writer.writeMapElement(
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


inline fun <Writer : JSONWriter> Writer.writeMapElement(
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


fun JSONWriter.writeMapNullElement(key: String) {
	writeMapKey(key)
	writeNull()
}


fun JSONWriter.writeNumberOrNull(value: Number?) =
	writeOrNull(value, JSONWriter::writeNumber)


inline fun <Writer : JSONWriter, Value : Any> Writer.writeOrNull(value: Value?, crossinline write: Writer.(value: Value) -> Unit) {
//	contract {
//		callsInPlace(write, InvocationKind.AT_MOST_ONCE)
//	}

	if (value != null)
		isolateValueWrite { write(value) }
	else
		writeNull()
}


fun JSONWriter.writeShortOrNull(value: Short?) =
	writeOrNull(value, JSONWriter::writeShort)


fun JSONWriter.writeStringOrNull(value: String?) =
	writeOrNull(value, JSONWriter::writeString)


fun JSONWriter.writeValueOrNull(value: Any?) =
	writeOrNull(value, JSONWriter::writeValue)
