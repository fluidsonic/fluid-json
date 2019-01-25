package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDate


object LocalDateJSONCodec : AbstractJSONCodec<LocalDate, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in LocalDate>) =
		readString().let { raw ->
			try {
				LocalDate.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalDate) =
		writeString(value.toString())
}
