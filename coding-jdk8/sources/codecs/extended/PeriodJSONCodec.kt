package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Period


object PeriodJSONCodec : AbstractJSONCodec<Period, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Period>) =
		readString().let { raw ->
			try {
				Period.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Period value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Period) =
		writeString(value.toString())
}
