package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalTime


object LocalTimeJSONCodec : AbstractJSONCodec<LocalTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in LocalTime>) =
		readString().let { raw ->
			try {
				LocalTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalTime value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalTime) =
		writeString(value.toString())
}
