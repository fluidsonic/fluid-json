package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Flushable
import java.io.Writer


interface JSONWriter : Closeable, Flushable {

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


	fun writeValue(value: Any?) {
		when (value) {
			is Array<*> -> writeList(value)
			is Boolean -> writeBoolean(value)
			is BooleanArray -> writeList(value)
			is Byte -> writeByte(value)
			is ByteArray -> writeList(value)
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
			null -> writeNull()
			else -> throw JSONException("Cannot write JSON value of ${value::class.java}: $value")
		}
	}


	companion object {

		operator fun invoke(destination: Writer): JSONWriter =
			StandardWriter(destination)
	}
}


fun JSONWriter.writeBooleanOrNull(value: Boolean?) =
	if (value != null) writeBoolean(value) else writeNull()


fun JSONWriter.writeByteOrNull(value: Byte?) =
	if (value != null) writeByte(value) else writeNull()


fun JSONWriter.writeDoubleOrNull(value: Double?) =
	if (value != null) writeDouble(value) else writeNull()


fun JSONWriter.writeFloatOrNull(value: Float?) =
	if (value != null) writeFloat(value) else writeNull()


fun JSONWriter.writeIntOrNull(value: Int?) =
	if (value != null) writeInt(value) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeIntoList(writeContent: Writer.() -> Unit) {
	writeListStart()
	writeContent()
	writeListEnd()
}


inline fun <Writer : JSONWriter> Writer.writeIntoMap(writeContent: Writer.() -> Unit) {
	writeMapStart()
	writeContent()
	writeMapEnd()
}


fun JSONWriter.writeList(value: BooleanArray) =
	writeListByElement(value) { writeBoolean(it) }


fun JSONWriter.writeList(value: ByteArray) =
	writeListByElement(value) { writeByte(it) }


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
	writeListByElement(value) { writeValue(it) }


fun JSONWriter.writeList(value: Iterable<*>) =
	writeListByElement(value) { writeValue(it) }


fun JSONWriter.writeList(value: Sequence<*>) =
	writeListByElement(value) { writeValue(it) }


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: BooleanArray,
	writeElement: Writer.(element: Boolean) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: ByteArray,
	writeElement: Writer.(element: Byte) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: DoubleArray,
	writeElement: Writer.(element: Double) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: FloatArray,
	writeElement: Writer.(element: Float) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: IntArray,
	writeElement: Writer.(element: Int) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: LongArray,
	writeElement: Writer.(element: Long) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter> Writer.writeListByElement(
	value: ShortArray,
	writeElement: Writer.(element: Short) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
	value: Array<Element>,
	writeElement: Writer.(element: Element) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
	value: Iterable<Element>,
	writeElement: Writer.(element: Element) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


inline fun <Writer : JSONWriter, Element> Writer.writeListByElement(
	value: Sequence<Element>,
	writeElement: Writer.(element: Element) -> Unit
) =
	writeIntoList {
		for (element in value)
			writeElement(element)
	}


fun JSONWriter.writeListOrNull(value: BooleanArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: ByteArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: DoubleArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: FloatArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: IntArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: LongArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: ShortArray?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: Array<*>?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: Iterable<*>?) =
	if (value != null) writeList(value) else writeNull()


fun JSONWriter.writeListOrNull(value: Sequence<*>?) =
	if (value != null) writeList(value) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: BooleanArray?,
	writeElement: Writer.(element: Boolean) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: ByteArray?,
	writeElement: Writer.(element: Byte) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: DoubleArray?,
	writeElement: Writer.(element: Double) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: FloatArray?,
	writeElement: Writer.(element: Float) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: IntArray?,
	writeElement: Writer.(element: Int) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: LongArray?,
	writeElement: Writer.(element: Long) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter> Writer.writeListOrNullByElement(
	value: ShortArray?,
	writeElement: Writer.(element: Short) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Array<Element>?,
	writeElement: Writer.(element: Element) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Iterable<Element>?,
	writeElement: Writer.(element: Element) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter, Element> Writer.writeListOrNullByElement(
	value: Sequence<Element>?,
	writeElement: Writer.(element: Element) -> Unit
) =
	if (value != null) writeListByElement(value, writeElement) else writeNull()


fun JSONWriter.writeLongOrNull(value: Long?) =
	if (value != null) writeLong(value) else writeNull()


fun JSONWriter.writeMap(value: Map<*, *>) =
	writeMapByElementValue(value) { writeValue(it) }


inline fun <Writer : JSONWriter, ElementKey, ElementValue> Writer.writeMapByElement(
	value: Map<ElementKey, ElementValue>,
	writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit
) =
	writeIntoMap {
		for ((elementKey, elementValue) in value)
			writeElement(elementKey, elementValue)
	}


inline fun <Writer : JSONWriter, ElementValue> Writer.writeMapByElementValue(
	value: Map<*, ElementValue>,
	writeElementValue: Writer.(value: ElementValue) -> Unit
) =
	writeMapByElement(value) { elementKey, elementValue ->
		writeMapKey(elementKey as? String ?: throw JSONException("Cannot write JSON key of ${value::class.java}: $value"))
		writeElementValue(elementValue)
	}


fun JSONWriter.writeMapOrNull(value: Map<*, *>?) =
	if (value != null) writeMap(value) else writeNull()


inline fun <Writer : JSONWriter, ElementKey, ElementValue> Writer.writeMapOrNullByElement(
	value: Map<ElementKey, ElementValue>?,
	writeElement: Writer.(key: ElementKey, value: ElementValue) -> Unit
) =
	if (value != null) writeMapByElement(value, writeElement) else writeNull()


inline fun <Writer : JSONWriter, ElementValue> Writer.writeMapOrNullByElementValue(
	value: Map<*, ElementValue>?,
	writeElementValue: Writer.(value: ElementValue) -> Unit
) =
	if (value != null) writeMapByElementValue(value, writeElementValue) else writeNull()


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


fun JSONWriter.writeMapElement(key: String, number: Number) {
	writeMapKey(key)
	writeNumber(number)
}


fun JSONWriter.writeMapElement(key: String, number: Number?, skipIfNull: Boolean = false) =
	if (number != null)
		writeMapElement(key, number)
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


fun JSONWriter.writeMapElement(key: String, string: String) {
	writeMapKey(key)
	writeString(string)
}


fun JSONWriter.writeMapElement(key: String, string: String?, skipIfNull: Boolean = false) =
	if (string != null)
		writeMapElement(key, string)
	else if (!skipIfNull)
		writeMapNullElement(key)
	else
		Unit


fun JSONWriter.writeMapNullElement(key: String) {
	writeString(key)
	writeNull()
}


fun JSONWriter.writeNumberOrNull(value: Number?) =
	if (value != null) writeNumber(value) else writeNull()


fun JSONWriter.writeShortOrNull(value: Short?) =
	if (value != null) writeShort(value) else writeNull()


fun JSONWriter.writeStringOrNull(value: String?) =
	if (value != null) writeString(value) else writeNull()
