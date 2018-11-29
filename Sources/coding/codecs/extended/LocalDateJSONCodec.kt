package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDate


object LocalDateJSONCodec : AbstractJSONCodec<LocalDate, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in LocalDate>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				LocalDate.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalDate value: $raw")
			}
		}


	override fun encode(value: LocalDate, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
