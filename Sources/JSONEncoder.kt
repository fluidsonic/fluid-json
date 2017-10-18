package com.github.fluidsonic.fluid.json


internal interface JSONEncoder<out Context : JSONCoderContext> : JSONWriter {

	val context: Context

	fun writeEncodable(value: Any)
}


internal fun JSONEncoder<*>.writeEncodableOrNull(value: Any?) {
	if (value == null) {
		writeNull()
		return
	}

	writeEncodable(value)
}


internal fun JSONEncoder<*>.writeMapEntry(name: String, encodable: Any) {
	writeString(name)
	writeEncodable(encodable)
}


internal fun JSONEncoder<*>.writeMapEntry(name: String, encodable: Any?, skipIfNull: Boolean = false) {
	if (encodable != null) {
		writeMapEntry(name, encodable)
	}
	else if (!skipIfNull) {
		writeMapNullEntry(name)
	}
}
