package com.github.fluidsonic.fluid.json

import java.time.*


object YearMonthJSONCodec : AbstractJSONCodec<YearMonth, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<YearMonth>) =
		readString().let { raw ->
			try {
				YearMonth.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date without day in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: YearMonth) =
		writeString(value.toString())
}
