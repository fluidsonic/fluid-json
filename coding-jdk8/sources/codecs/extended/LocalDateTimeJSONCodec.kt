package com.github.fluidsonic.fluid.json

import java.time.*


object LocalDateTimeJSONCodec : AbstractJSONCodec<LocalDateTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<LocalDateTime>) =
		readString().let { raw ->
			try {
				LocalDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date and time in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalDateTime) =
		writeString(value.toString())
}
