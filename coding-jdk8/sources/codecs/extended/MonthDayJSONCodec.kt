package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.MonthDay


object MonthDayJSONCodec : AbstractJSONCodec<MonthDay, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<MonthDay>) =
		readString().let { raw ->
			try {
				MonthDay.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date without year in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: MonthDay) =
		writeString(value.toString())
}
