package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDateTime


object LocalDateTimeJSONCodec : AbstractJSONCodec<LocalDateTime, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in LocalDateTime>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				LocalDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalDateTime value: $raw")
			}
		}


	override fun encode(value: LocalDateTime, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
