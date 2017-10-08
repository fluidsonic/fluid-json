package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONWriter {

	val previousToken: JSONToken?

	fun writeBoolean(value: Boolean)
	fun writeBooleanOrNull(value: Boolean?)
	fun writeDouble(value: Double)
	fun writeDoubleOrNull(value: Double?)
	fun writeFloat(value: Float)
	fun writeFloatOrNull(value: Float?)
	fun writeInt(value: Int)
	fun writeIntOrNull(value: Int?)
	fun writeListEnd()
	fun writeListStart()
	fun writeLong(value: Long)
	fun writeLongOrNUll(value: Long?)
	fun writeMapEnd()
	fun writeMapStart()
	fun writeNull()
	fun writeString(value: String)
	fun writeStringOrNull(value: String?)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, boolean: Boolean) {
	writeString(name)
	writeBoolean(boolean)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, boolean: Boolean?, skipIfNull: Boolean = false) {
	if (boolean != null) {
		writeEntry(name, boolean)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, double: Double) {
	writeString(name)
	writeDouble(double)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, double: Double?, skipIfNull: Boolean = false) {
	if (double != null) {
		writeEntry(name, double)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, float: Float) {
	writeString(name)
	writeFloat(float)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, float: Float?, skipIfNull: Boolean = false) {
	if (float != null) {
		writeEntry(name, float)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, int: Int) {
	writeString(name)
	writeInt(int)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, int: Int?, skipIfNull: Boolean = false) {
	if (int != null) {
		writeEntry(name, int)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, long: Long) {
	writeString(name)
	writeLong(long)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, long: Long?, skipIfNull: Boolean = false) {
	if (long != null) {
		writeEntry(name, long)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, string: String) {
	writeString(name)
	writeString(string)
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeEntry(name: String, string: String?, skipIfNull: Boolean = false) {
	if (string != null) {
		writeEntry(name, string)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal fun JSONWriter.writeNullEntry(name: String) {
	writeString(name)
	writeNull()
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <Writer : JSONWriter> Writer.writeMap(write: Writer.() -> Unit) {
	writeMapStart()
	write()
	writeMapEnd()
}
