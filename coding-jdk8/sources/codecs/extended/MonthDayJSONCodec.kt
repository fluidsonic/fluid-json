package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.MonthDay


object MonthDayJSONCodec : AbstractJSONCodec<MonthDay, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in MonthDay>) =
		readString().let { raw ->
			try {
				MonthDay.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse MonthDay value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: MonthDay) =
		writeString(value.toString())
}
