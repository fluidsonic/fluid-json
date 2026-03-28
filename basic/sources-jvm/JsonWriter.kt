@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.fluidsonic.json

import java.io.*
import kotlin.contracts.*
import kotlin.internal.*


/**
 * Writes JSON tokens and values to a destination in a streaming fashion.
 */
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


/** Writes exactly one value in isolation, ensuring no additional tokens are emitted. */
public inline fun <Writer : JsonWriter> Writer.isolateValueWrite(crossinline write: Writer.() -> Unit) {
	contract {
		callsInPlace(write, InvocationKind.EXACTLY_ONCE)
	}

	val depth = beginValueIsolation()
	val value = write()
	endValueIsolation(depth = depth)

	return value
}


/** Executes [block] with this writer, then terminates or closes it depending on [withTermination]. */
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


/** Executes [block] and optionally terminates this writer afterward based on [withTermination]. */
public inline fun <Writer : JsonWriter, Result> Writer.withTermination(withTermination: Boolean = true, block: Writer.() -> Result): Result {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

	return if (withTermination)
		use { it.block() }
	else
		block()
}


/** Executes [block], marking this writer as errored if an exception is thrown. */
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


/** Writes a JSON boolean [value], or a JSON null if the value is `null`. */
public fun JsonWriter.writeBooleanOrNull(value: Boolean?) {
	writeOrNull(value, JsonWriter::writeBoolean)
}


/** Writes a [Byte] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeByteOrNull(value: Byte?) {
	writeOrNull(value, JsonWriter::writeByte)
}


/** Writes a [Char] [value] as a JSON string, or a JSON null if the value is `null`. */
public fun JsonWriter.writeCharOrNull(value: Char?) {
	writeOrNull(value, JsonWriter::writeChar)
}


/** Writes a [Double] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeDoubleOrNull(value: Double?) {
	writeOrNull(value, JsonWriter::writeDouble)
}


/** Writes a [Float] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeFloatOrNull(value: Float?) {
	writeOrNull(value, JsonWriter::writeFloat)
}


/** Writes an [Int] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeIntOrNull(value: Int?) {
	writeOrNull(value, JsonWriter::writeInt)
}


/** Writes a complete JSON list, invoking [writeContent] between list start and end tokens. */
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


/** Writes a complete JSON map, invoking [writeContent] between map start and end tokens. */
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


/** Writes a [BooleanArray] as a JSON list. */
public fun JsonWriter.writeList(value: BooleanArray) {
	writeListByElement(value) { writeBoolean(it) }
}


/** Writes a [ByteArray] as a JSON list. */
public fun JsonWriter.writeList(value: ByteArray) {
	writeListByElement(value) { writeByte(it) }
}


/** Writes a [CharArray] as a JSON list. */
public fun JsonWriter.writeList(value: CharArray) {
	writeListByElement(value) { writeChar(it) }
}


/** Writes a [DoubleArray] as a JSON list. */
public fun JsonWriter.writeList(value: DoubleArray) {
	writeListByElement(value) { writeDouble(it) }
}


/** Writes a [FloatArray] as a JSON list. */
public fun JsonWriter.writeList(value: FloatArray) {
	writeListByElement(value) { writeFloat(it) }
}


/** Writes an [IntArray] as a JSON list. */
public fun JsonWriter.writeList(value: IntArray) {
	writeListByElement(value) { writeInt(it) }
}


/** Writes a [LongArray] as a JSON list. */
public fun JsonWriter.writeList(value: LongArray) {
	writeListByElement(value) { writeLong(it) }
}


/** Writes a [ShortArray] as a JSON list. */
public fun JsonWriter.writeList(value: ShortArray) {
	writeListByElement(value) { writeShort(it) }
}


/** Writes an [Array] as a JSON list. */
public fun JsonWriter.writeList(value: Array<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


/** Writes an [Iterable] as a JSON list. */
public fun JsonWriter.writeList(value: Iterable<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


/** Writes a [Sequence] as a JSON list. */
public fun JsonWriter.writeList(value: Sequence<*>) {
	writeListByElement(value) { writeValueOrNull(it) }
}


/** Writes a [BooleanArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [ByteArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [CharArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [DoubleArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [FloatArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes an [IntArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [LongArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [ShortArray] as a JSON list, using [writeElement] for each element. */
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


/** Writes an [Array] as a JSON list, using [writeElement] for each element. */
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


/** Writes an [Iterable] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [Sequence] as a JSON list, using [writeElement] for each element. */
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


/** Writes a [BooleanArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: BooleanArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [ByteArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: ByteArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [CharArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: CharArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [DoubleArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: DoubleArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [FloatArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: FloatArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes an [IntArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: IntArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [LongArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: LongArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [ShortArray] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: ShortArray?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes an [Array] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: Array<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes an [Iterable] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: Iterable<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [Sequence] as a JSON list, or a JSON null if the value is `null`. */
public fun JsonWriter.writeListOrNull(value: Sequence<*>?) {
	writeOrNull(value, JsonWriter::writeList)
}


/** Writes a [BooleanArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: BooleanArray?,
	crossinline writeElement: Writer.(element: Boolean) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [ByteArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ByteArray?,
	crossinline writeElement: Writer.(element: Byte) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [CharArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: CharArray?,
	crossinline writeElement: Writer.(element: Char) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [DoubleArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: DoubleArray?,
	crossinline writeElement: Writer.(element: Double) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [FloatArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: FloatArray?,
	crossinline writeElement: Writer.(element: Float) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes an [IntArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: IntArray?,
	crossinline writeElement: Writer.(element: Int) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [LongArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: LongArray?,
	crossinline writeElement: Writer.(element: Long) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [ShortArray] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter> Writer.writeListOrNullByElement(
	value: ShortArray?,
	crossinline writeElement: Writer.(element: Short) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes an [Array] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Array<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes an [Iterable] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Iterable<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [Sequence] as a JSON list using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, Element> Writer.writeListOrNullByElement(
	value: Sequence<Element>?,
	crossinline writeElement: Writer.(element: Element) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeListByElement(it, writeElement) }
}


/** Writes a [Long] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeLongOrNull(value: Long?) {
	writeOrNull(value, JsonWriter::writeLong)
}


/** Writes a [Map] as a JSON map. */
public fun JsonWriter.writeMap(value: Map<*, *>) {
	writeMapByElementValue(value) { writeValueOrNull(it) }
}


/** Writes a [Map] as a JSON map, using [writeElement] for each entry. */
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


/** Writes a [Map] as a JSON map, using [writeElementValue] to write each value. */
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


/** Writes a [Map] as a JSON map, or a JSON null if the value is `null`. */
public fun JsonWriter.writeMapOrNull(value: Map<*, *>?) {
	writeOrNull(value, JsonWriter::writeMap)
}


/** Writes a [Map] as a JSON map using [writeElement], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, ElementKey, ElementValue> Writer.writeMapOrNullByElement(
	value: Map<ElementKey, ElementValue>?,
	crossinline writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElement, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeMapByElement(it, writeElement) }
}


/** Writes a [Map] as a JSON map using [writeElementValue], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, ElementValue> Writer.writeMapOrNullByElementValue(
	value: Map<*, ElementValue>?,
	crossinline writeElementValue: Writer.(value: ElementValue) -> Unit,
) {
	contract {
		callsInPlace(writeElementValue, InvocationKind.UNKNOWN)
	}

	writeOrNull(value) { writeMapByElementValue(it, writeElementValue) }
}


/** Writes a map element with the given [key] and boolean value. */
public fun JsonWriter.writeMapElement(key: String, boolean: Boolean) {
	writeMapKey(key)
	writeBoolean(boolean)
}


/** Writes a map element with the given [key] and nullable boolean value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, boolean: Boolean?, skipIfNull: Boolean = false) {
	if (boolean != null)
		writeMapElement(key, boolean)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and [Byte] value. */
public fun JsonWriter.writeMapElement(key: String, byte: Byte) {
	writeMapKey(key)
	writeByte(byte)
}


/** Writes a map element with the given [key] and nullable [Byte] value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, byte: Byte?, skipIfNull: Boolean = false) {
	if (byte != null)
		writeMapElement(key, byte)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and [Char] value. */
public fun JsonWriter.writeMapElement(key: String, char: Char) {
	writeMapKey(key)
	writeChar(char)
}


/** Writes a map element with the given [key] and nullable [Char] value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, char: Char?, skipIfNull: Boolean = false) {
	if (char != null)
		writeMapElement(key, char)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and [Double] value. */
public fun JsonWriter.writeMapElement(key: String, double: Double) {
	writeMapKey(key)
	writeDouble(double)
}


/** Writes a map element with the given [key] and nullable [Double] value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, double: Double?, skipIfNull: Boolean = false) {
	if (double != null)
		writeMapElement(key, double)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and [Float] value. */
public fun JsonWriter.writeMapElement(key: String, float: Float) {
	writeMapKey(key)
	writeFloat(float)
}


/** Writes a map element with the given [key] and nullable [Float] value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, float: Float?, skipIfNull: Boolean = false) {
	if (float != null)
		writeMapElement(key, float)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and [Int] value. */
public fun JsonWriter.writeMapElement(key: String, int: Int) {
	writeMapKey(key)
	writeInt(int)
}


/** Writes a map element with the given [key] and nullable [Int] value, optionally skipping null values. */
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


/** Writes a map element with the given [key] and an [Array] value as a JSON list, optionally skipping null values. */
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


/** Writes a map element with the given [key] and an [Iterable] value as a JSON list, optionally skipping null values. */
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


/** Writes a map element with the given [key] and a [Sequence] value as a JSON list, optionally skipping null values. */
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


/** Writes a map element with the given [key] and [Long] value. */
public fun JsonWriter.writeMapElement(key: String, long: Long) {
	writeMapKey(key)
	writeLong(long)
}


/** Writes a map element with the given [key] and nullable [Long] value, optionally skipping null values. */
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


/** Writes a map element with the given [key] and a [Map] value as a JSON map, optionally skipping null values. */
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


/** Writes a map element with the given [key] and nullable [Number] value, optionally skipping null values. */
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


/** Writes a map element with the given [key] and [Short] value. */
public fun JsonWriter.writeMapElement(key: String, short: Short) {
	writeMapKey(key)
	writeShort(short)
}


/** Writes a map element with the given [key] and nullable [Short] value, optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, short: Short?, skipIfNull: Boolean = false) {
	if (short != null)
		writeMapElement(key, short)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit
}


/** Writes a map element with the given [key] and nullable [String] value, optionally skipping null values. */
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


/** Writes a map element with the given [key] and any [value], optionally skipping null values. */
public fun JsonWriter.writeMapElement(key: String, value: Any?, skipIfNull: Boolean = false) {
	writeMapElement(key, value, skipIfNull) { writeValue(it) }
}


/** Writes a map element with the given [key] using [writeCustomValue], optionally skipping null values. */
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


/** Writes a map element with the given [key], using [writeValue] to write the value. */
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


/** Writes a map element with the given [key] and a JSON null value. */
public fun JsonWriter.writeMapNullElement(key: String) {
	writeMapKey(key)
	writeNull()
}


/** Writes a [Number] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeNumberOrNull(value: Number?) {
	writeOrNull(value, JsonWriter::writeNumber)
}


/** Writes the [value] using [write], or a JSON null if the value is `null`. */
public inline fun <Writer : JsonWriter, Value : Any> Writer.writeOrNull(value: Value?, crossinline write: Writer.(value: Value) -> Unit) {
	contract {
		callsInPlace(write, InvocationKind.AT_MOST_ONCE)
	}

	if (value != null)
		isolateValueWrite { write(value) }
	else
		writeNull()
}


/** Writes a [Short] [value] as a JSON number, or a JSON null if the value is `null`. */
public fun JsonWriter.writeShortOrNull(value: Short?) {
	writeOrNull(value, JsonWriter::writeShort)
}


/** Writes a [String] [value] as a JSON string, or a JSON null if the value is `null`. */
public fun JsonWriter.writeStringOrNull(value: String?) {
	writeOrNull(value, JsonWriter::writeString)
}


/** Writes any [value] as JSON, or a JSON null if the value is `null`. */
public fun JsonWriter.writeValueOrNull(value: Any?) {
	writeOrNull(value, JsonWriter::writeValue)
}
