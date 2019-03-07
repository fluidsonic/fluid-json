package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetDateTime


object OffsetDateTimeJSONCodec : AbstractJSONCodec<OffsetDateTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<OffsetDateTime>) =
		readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: OffsetDateTime) =
		writeString(value.toString())
}
