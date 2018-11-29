package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Period


object PeriodJSONCodec : AbstractJSONCodec<Period, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Period>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				Period.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Period value: $raw")
			}
		}


	override fun encode(value: Period, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
