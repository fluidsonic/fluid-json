package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.MonthDay


object MonthDayJSONCodec : AbstractJSONCodec<MonthDay, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in MonthDay>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				MonthDay.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse MonthDay value: $raw")
			}
		}


	override fun encode(value: MonthDay, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
