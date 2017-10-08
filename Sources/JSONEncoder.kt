package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal class JSONEncoder<Context : JSONCoderContext>(
	val codecs: JSONCodecRegistry<Context>,
	val context: Context,
	writer: JSONWriter
) : JSONWriter by writer {

	@API(status = API.Status.EXPERIMENTAL)
	inline fun <reified Value : Any> writeEncodable(encodable: Value) {
		writeEncodableOrNull(encodable)
	}


	@API(status = API.Status.EXPERIMENTAL)
	inline fun <reified Value : Any> writeEncodableOrNull(encodable: Value?) {
		if (encodable == null) {
			writeNull()
			return
		}

		val codec = codecs.encoderCodecForClass(Value::class.java)
			?: throw JSONException("no encoder codec registered for ${Value::class.java}")

		return codec.encode(encodable, encoder = this)
	}
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <reified Value : Any> JSONEncoder<*>.writeEntry(name: String, encodable: Value) {
	writeString(name)
	writeEncodable(encodable)
}


@API(status = API.Status.EXPERIMENTAL)
internal inline fun <reified Value : Any> JSONEncoder<*>.writeEntry(name: String, encodable: Value?, skipIfNull: Boolean = false) {
	if (encodable != null) {
		writeEntry(name, encodable)
	}
	else if (!skipIfNull) {
		writeNullEntry(name)
	}
}
