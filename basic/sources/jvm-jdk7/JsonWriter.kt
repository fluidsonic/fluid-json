@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.fluidsonic.json

import java.io.*
import kotlin.contracts.*
import kotlin.internal.*


public interface JsonWriter : Closeable, Flushable {

	public val depth: JsonDepth
	public val isErrored: Boolean
	public val isInValueIsolation: Boolean
	public val path: JsonPath

	public fun beginValueIsolation(): JsonDepth
	public fun endValueIsolation(depth: JsonDepth)
	public fun markAsErrored()
	public fun terminate()
	public fun writeBoolean(value: Boolean)
	public fun writeDouble(value: Double)
	public fun writeListEnd()
	public fun writeListStart()
	public fun writeLong(value: Long)
	public fun writeMapEnd()
	public fun writeMapStart()
	public fun writeNull()
	public fun writeString(value: String)


	public fun writeByte(value: Byte) {
		writeLong(value.toLong())
	}


	public fun writeChar(value: Char) {
		writeString(value.toString())
	}


	public fun writeFloat(value: Float) {
		writeDouble(value.toDouble())
	}


	public fun writeInt(value: Int) {
		writeLong(value.toLong())
	}


	public fun writeMapKey(value: String) {
		writeString(value)
	}


	public fun writeNumber(value: Number) {
		return when (value) {
			is Byte -> writeByte(value)
			is Float -> writeFloat(value)
			is Int -> writeInt(value)
			is Long -> writeLong(value)
			is Short -> writeShort(value)
			else -> writeDouble(value.toDouble())
		}
	}


	public fun writeShort(value: Short) {
		writeLong(value.toLong())
	}


	public fun writeValue(value: Any) {
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


	public companion object {

		public fun build(destination: Writer): JsonWriter =
			StandardWriter(destination)
	}
}


public inline fun <Writer : JsonWriter> Writer.isolateValueWrite(crossinline write: Writer.() -> Unit) {
	val depth = beginValueIsolation()
	val value = write()
	endValueIsolation(depth = depth)

	return value
}


public inline fun <Writer : JsonWriter?, Result> Writer.use(withTermination: Boolean = true, block: (Writer) -> Result): Result {
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


public inline fun <Writer : JsonWriter, Result> Writer.withTermination(withTermination: Boolean = true, block: Writer.() -> Result): Result {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}


public inline fun <Writer : JsonWriter, ReturnValue> Writer.withErrorChecking(block: Writer.() -> ReturnValue): ReturnValue {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

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


public fun JsonWriter.writeBooleanOrNull(value: Boolean?) {
	writeOrNull(value, JsonWriter::writeBoolean)
}


public fun JsonWriter.writeByteOrNull(value: Byte?) {
	writeOrNull(value, JsonWriter::writeByte)
}


public fun JsonWriter.writeCharOrNull(value: Char?) {
	writeOrNull(value, JsonWriter::writeChar)
}


public fun JsonWriter.writeDoubleOrNull(value: Double?) {
	writeOrNull(value, JsonWriter::writeDouble)
}


public fun JsonWriter.writeFloatOrNull(value: Float?) {
	writeOrNull(value, JsonWriter::writeFloat)
}


public fun JsonWriter.writeIntOrNull(value: Int?) {
	writeOrNull(value, JsonWriter::writeInt)
}


public inline fun <Writer : JsonWriter> Writer.writeIntoList(crossinline writeContent: Writer.() -> Unit) {
	contract {
		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
	}

	isolateValueWrite {
		writeListStart()
		writeContent()
		writeListEnd()
	}
}


public inline fun <Writer : JsonWriter> Writer.writeIntoMap(crossinline writeContent: Writer.() -> Unit) {
	contract {
		callsInPlace(writeContent, InvocationKind.EXACTLY_ONCE)
	}

	isolateValueWrite {
		writeMapStart()
		writeContent()
		writeMapEnd()
	}
}


public fun JsonWriter.writeList(value: BooleanArray) {
	writeListByElement(value) { writeBoolean(it) }
}


public fun JsonWriter.writeList(value: ByteArray) {
	writeListByElement(value) { writeByte(it) }
}


public fun JsonWriter.writeList(value: CharArray) {
	writeListByElement(value) { writeChar(it) }
}


public fun JsonWriter.writeList(value: DoubleArray) {
	writeListByElement(value) { writeDouble(it) }
}


public fun JsonWriter.writeList(value: FloatArray) {
	writeListByElement(value) { writeFloat(it) }
}


public fun JsonWriter.writeList(value: IntArray) {
	writeListByElement(value) { writeInt(it) }
}


public fun JsonWriter.writeList(value: LongArray) {
	writeListByElement(value) { writeLong(it) }
}


public fun JsonWriter.writeList(value: ShortArray) {
	writeListByElement(value) { writeShort(it) }
}


public fun JsonWriter.writeList(value: Array<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


public fun JsonWriter.writeList(value: Iterable<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


public fun JsonWriter.writeList(value: Sequence<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: BooleanArray,
	crossinline writeElement: Writer.(element: Boolean) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: ByteArray,
	crossinline writeElement: Writer.(element: Byte) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: CharArray,
	crossinline writeElement: Writer.(element: Char) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: DoubleArray,
	crossinline writeElement: Writer.(element: Double) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: FloatArray,
	crossinline writeElement: Writer.(element: Float) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: IntArray,
	crossinline writeElement: Writer.(element: Int) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: LongArray,
	crossinline writeElement: Writer.(element: Long) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter> Writer.writeListByElement(
	value: ShortArray,
	crossinline writeElement: Writer.(element: Short) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Array<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Iterable<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListByElement(
	value: Sequence<Element>,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoList {
		for (element in value) {
			isolateValueWrite {
				writeElement(element)
			}
		}
	}
}


public fun JsonWriter.writeListOrNull(value: BooleanArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: ByteArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: CharArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: DoubleArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: FloatArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: IntArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: LongArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: ShortArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: Array<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: Iterable<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


public fun JsonWriter.writeListOrNull(value: Sequence<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: BooleanArray?,
	crossinline writeElement: Writer.(element: Boolean) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ByteArray?,
	crossinline writeElement: Writer.(element: Byte) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: CharArray?,
	crossinline writeElement: Writer.(element: Char) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: DoubleArray?,
	crossinline writeElement: Writer.(element: Double) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: FloatArray?,
	crossinline writeElement: Writer.(element: Float) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: IntArray?,
	crossinline writeElement: Writer.(element: Int) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: LongArray?,
	crossinline writeElement: Writer.(element: Long) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ShortArray?,
	crossinline writeElement: Writer.(element: Short) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Array<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Iterable<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Sequence<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


public fun JsonWriter.writeLongOrNull(value: Long?) {
	writeOrNull(value, JsonWriter::writeLong)
}


public fun JsonWriter.writeMap(value: Map<*, *>) {
	writeMapByElementValue(value) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter, ElementKey, ElementValue> Writer.writeMapByElement(
	value: Map<ElementKey, ElementValue>,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeIntoMap {
		for ((elementKey, elementValue) in value)
			writeElement(elementKey, elementValue)
	}
}


public inline fun <Writer : JsonWriter, ElementValue> Writer.writeMapByElementValue(
	value: Map<*, ElementValue>,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
	}

	writeMapByElement(value) { elementKey, elementValue ->
		writeValueOrNull(elementKey)
		isolateValueWrite {
			writeElementValue(elementValue)
		}
	}
}


public fun JsonWriter.writeMapOrNull(value: Map<*, *>?) {
	writeOrNull(value, JsonWriter::writeMap)
}


public inline fun <Writer : JsonWriter, ElementKey, ElementValue> Writer.writeMapOrNullByElement(
	value: Map<ElementKey, ElementValue>?,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeMapByElement(it, writeElement) }
}


public inline fun <Writer : JsonWriter, ElementValue> Writer.writeMapOrNullByElementValue(
	value: Map<*, ElementValue>?,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeMapByElementValue(it, writeElementValue) }
}


public fun JsonWriter.writeMapElement(key: String, boolean: Boolean) {
	writeMapKey(key)
	writeBoolean(boolean)
}


public fun JsonWriter.writeMapElement(key: String, boolean: Boolean?, skipIfNull: Boolean = false) {
	if (boolean != null)
		writeMapElement(key, boolean)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, byte: Byte) {
	writeMapKey(key)
	writeByte(byte)
}


public fun JsonWriter.writeMapElement(key: String, byte: Byte?, skipIfNull: Boolean = false) {
	if (byte != null)
		writeMapElement(key, byte)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, char: Char) {
	writeMapKey(key)
	writeChar(char)
}


public fun JsonWriter.writeMapElement(key: String, char: Char?, skipIfNull: Boolean = false) {
	if (char != null)
		writeMapElement(key, char)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, double: Double) {
	writeMapKey(key)
	writeDouble(double)
}


public fun JsonWriter.writeMapElement(key: String, double: Double?, skipIfNull: Boolean = false) {
	if (double != null)
		writeMapElement(key, double)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, float: Float) {
	writeMapKey(key)
	writeFloat(float)
}


public fun JsonWriter.writeMapElement(key: String, float: Float?, skipIfNull: Boolean = false) {
	if (float != null)
		writeMapElement(key, float)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, int: Int) {
	writeMapKey(key)
	writeInt(int)
}


public fun JsonWriter.writeMapElement(key: String, int: Int?, skipIfNull: Boolean = false) {
	if (int != null)
		writeMapElement(key, int)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: Array<*>?, skipIfNull: Boolean = false) {
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Array<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: BooleanArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: ByteArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: CharArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: DoubleArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: FloatArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: IntArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: Iterable<*>?, skipIfNull: Boolean = false) {
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Iterable<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: LongArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: Sequence<*>?, skipIfNull: Boolean = false) {
	writeMapElement(key, list = list, skipIfNull = skipIfNull) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter, Element> Writer.writeMapElement(
	key: String,
	list: Sequence<Element>?,
	skipIfNull: Boolean = false,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	if (list != null) {
		writeMapKey(key)
		writeListByElement(list, writeElement)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = list, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, list: ShortArray?, skipIfNull: Boolean = false) {
	if (list != null) {
		writeMapKey(key)
		writeList(list)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, long: Long) {
	writeMapKey(key)
	writeLong(long)
}


public fun JsonWriter.writeMapElement(key: String, long: Long?, skipIfNull: Boolean = false) {
	if (long != null)
		writeMapElement(key, long)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


@Deprecated(
	message = "Dangerous overload. Use 'Any' overload so that codecs are properly applied.",
	level = DeprecationLevel.ERROR,
	replaceWith = ReplaceWith(expression = "writeMapElement(key, value = map, skipIfNull = skipIfNull)")
)
@LowPriorityInOverloadResolution
public fun JsonWriter.writeMapElement(key: String, map: Map<*, *>?, skipIfNull: Boolean = false) {
	writeMapElement(key, map = map, skipIfNull = skipIfNull) { writeValueOrNull(it) }
}


public inline fun <Writer : JsonWriter, Child> Writer.writeMapElement(
	key: String,
	map: Map<*, Child>?,
	skipIfNull: Boolean = false,
	crossinline writeChild: Writer.(value: Child) -> Unit,
) {
	if (map != null) {
		writeMapKey(key)
		writeMapByElementValue(map, writeChild)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, number: Number?, skipIfNull: Boolean = false) {
	if (number != null) {
		writeMapKey(key)
		writeNumber(number)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, short: Short) {
	writeMapKey(key)
	writeShort(short)
}


public fun JsonWriter.writeMapElement(key: String, short: Short?, skipIfNull: Boolean = false) {
	if (short != null)
		writeMapElement(key, short)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, string: String?, skipIfNull: Boolean = false) {
	if (string != null) {
		writeMapKey(key)
		writeString(string)
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


public fun JsonWriter.writeMapElement(key: String, value: Any?, skipIfNull: Boolean = false) {
	writeMapElement(key, value, skipIfNull) { writeValue(it) }
}


public inline fun <Writer : JsonWriter, Value : Any> Writer.writeMapElement(
	key: String,
	value: Value?,
	skipIfNull: Boolean = false,
	crossinline writeCustomValue: Writer.(value: Value) -> Unit,
) {
	contract {
		callsInPlace(writeCustomValue, InvocationKind.AT_MOST_ONCE)
	}

	if (value != null) {
		writeMapKey(key)
		isolateValueWrite {
			writeCustomValue(value)
		}
	}
	else if (!skipIfNull)
		writeMapNullElement(key)
}


public inline fun <Writer : JsonWriter> Writer.writeMapElement(
	key: String,
	crossinline writeValue: Writer.() -> Unit,
) {
	contract {
		callsInPlace(writeValue, InvocationKind.EXACTLY_ONCE)
	}

	writeMapKey(key)
	isolateValueWrite {
		writeValue()
	}
}


public fun JsonWriter.writeMapNullElement(key: String) {
	writeMapKey(key)
	writeNull()
}


public fun JsonWriter.writeNumberOrNull(value: Number?) {
	writeOrNull(value, JsonWriter::writeNumber)
}


public inline fun <Writer : JsonWriter, Value : Any> Writer.writeOrNull(value: Value?, crossinline write: Writer.(value: Value) -> Unit) {
	contract {
		callsInPlace(write, InvocationKind.AT_MOST_ONCE)
	}

	if (value != null)
		isolateValueWrite { write(value) }
	else
		writeNull()
}


public fun JsonWriter.writeShortOrNull(value: Short?) {
	writeOrNull(value, JsonWriter::writeShort)
}


public fun JsonWriter.writeStringOrNull(value: String?) {
	writeOrNull(value, JsonWriter::writeString)
}


public fun JsonWriter.writeValueOrNull(value: Any?) {
	writeOrNull(value, JsonWriter::writeValue)
}
