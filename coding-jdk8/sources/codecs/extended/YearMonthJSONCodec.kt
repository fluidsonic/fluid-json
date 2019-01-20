package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.YearMonth


object YearMonthJSONCodec : AbstractJSONCodec<YearMonth, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in YearMonth>) =
		readString().let { raw ->
			try {
				YearMonth.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse YearMonth value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: YearMonth) =
		writeString(value.toString())
}
