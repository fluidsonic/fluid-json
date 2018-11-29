package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDateTime


object LocalDateTimeJSONCodec : AbstractJSONCodec<LocalDateTime, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in LocalDateTime>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				LocalDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalDateTime value: $raw")
			}
		}


	override fun encode(value: LocalDateTime, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
