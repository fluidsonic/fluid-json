package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDateTime


object LocalDateTimeJSONCodec : AbstractJSONCodec<LocalDateTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in LocalDateTime>) =
		readString().let { raw ->
			try {
				LocalDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalDateTime value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalDateTime) =
		writeString(value.toString())
}
