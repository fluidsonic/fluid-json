package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


// FIXME closable?
internal interface JSONWriter {

	fun writeBoolean(value: Boolean)
	fun writeDouble(value: Double)
	fun writeListEnd()
	fun writeListStart()
	fun writeLong(value: Long)
	fun writeMapEnd()
	fun writeMapStart()
	fun writeNull()
	fun writeString(value: String)


	fun writeByte(value: Byte) {
		writeLong(value.toLong())
	}


	fun writeFloat(value: Float) {
		writeDouble(value.toDouble())
	}


	fun writeInt(value: Int) {
		writeLong(value.toLong())
	}


	fun writeShort(value: Short) {
		writeLong(value.toLong())
	}


	fun writeNumber(value: Number) {
		when (value) {
			is Byte -> writeByte(value)
			is Float -> writeFloat(value)
			is Int -> writeInt(value)
			is Long -> writeLong(value)
			is Short -> writeShort(value)
			else -> writeDouble(value.toDouble())
		}
	}


	fun writeKey(value: String) {
		writeString(value)
	}
}


internal fun JSONWriter.writeBooleanOrNull(value: Boolean?) {
	if (value != null) {
		writeBoolean(value)
	}
	else {
		writeNull()
	}
}


internal fun JSONWriter.writeByteOrNull(value: Byte?) {
	if (value != null) {
		writeByte(value)
	}
	else {
		writeNull()
	}
}


internal fun JSONWriter.writeDoubleOrNull(value: Double?) {
	if (value != null) {
		writeDouble(value)
	}
	else {
		writeNull()
	}
}


internal fun JSONWriter.writeFloatOrNull(value: Float?) {
	if (value != null) {
		writeFloat(value)
	}
	else {
		writeNull()
	}
}


internal fun JSONWriter.writeIntOrNull(value: Int?) {
	if (value != null) {
		writeInt(value)
	}
	else {
		writeNull()
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Writer : JSONWriter> Writer.writeList(write: Writer.() -> Unit) {
	writeListStart()
	write()
	writeListEnd()
}


internal fun JSONWriter.writeLongOrNull(value: Long?) {
	if (value != null) {
		writeLong(value)
	}
	else {
		writeNull()
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Writer : JSONWriter> Writer.writeMap(write: Writer.() -> Unit) {
	writeMapStart()
	write()
	writeMapEnd()
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, boolean: Boolean) {
	writeString(name)
	writeBoolean(boolean)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, boolean: Boolean?, skipIfNull: Boolean = false) {
	if (boolean != null) {
		writeMapEntry(name, boolean)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, byte: Byte) {
	writeString(name)
	writeByte(byte)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, byte: Byte?, skipIfNull: Boolean = false) {
	if (byte != null) {
		writeMapEntry(name, byte)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, double: Double) {
	writeString(name)
	writeDouble(double)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, double: Double?, skipIfNull: Boolean = false) {
	if (double != null) {
		writeMapEntry(name, double)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, float: Float) {
	writeString(name)
	writeFloat(float)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, float: Float?, skipIfNull: Boolean = false) {
	if (float != null) {
		writeMapEntry(name, float)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, int: Int) {
	writeString(name)
	writeInt(int)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, int: Int?, skipIfNull: Boolean = false) {
	if (int != null) {
		writeMapEntry(name, int)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


// FIXME map entry?
@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, long: Long) {
	writeString(name)
	writeLong(long)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, long: Long?, skipIfNull: Boolean = false) {
	if (long != null) {
		writeMapEntry(name, long)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, short: Short) {
	writeString(name)
	writeShort(short)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, short: Short?, skipIfNull: Boolean = false) {
	if (short != null) {
		writeMapEntry(name, short)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, string: String) {
	writeString(name)
	writeString(string)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapEntry(name: String, string: String?, skipIfNull: Boolean = false) {
	if (string != null) {
		writeMapEntry(name, string)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeMapNullEntry(name: String) {
	writeString(name)
	writeNull()
}


internal fun JSONWriter.writeShortOrNull(value: Short?) {
	if (value != null) {
		writeShort(value)
	}
	else {
		writeNull()
	}
}


internal fun JSONWriter.writeStringOrNull(value: String?) {
	if (value != null) {
		writeString(value)
	}
	else {
		writeNull()
	}
}
