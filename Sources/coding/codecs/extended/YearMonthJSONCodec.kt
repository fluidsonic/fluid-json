package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.YearMonth


object YearMonthJSONCodec : AbstractJSONCodec<YearMonth, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in YearMonth>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				YearMonth.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse YearMonth value: $raw")
			}
		}


	override fun encode(value: YearMonth, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
