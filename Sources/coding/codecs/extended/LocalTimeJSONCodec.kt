package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalTime


object LocalTimeJSONCodec : AbstractJSONCodec<LocalTime, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in LocalTime>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				LocalTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalTime value: $raw")
			}
		}


	override fun encode(value: LocalTime, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
