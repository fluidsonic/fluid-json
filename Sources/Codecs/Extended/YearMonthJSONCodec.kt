package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.YearMonth


object YearMonthJSONCodec : AbstractJSONCodec<YearMonth, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in YearMonth>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				YearMonth.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse YearMonth value: $raw")
			}
		}


	override fun encode(value: YearMonth, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
