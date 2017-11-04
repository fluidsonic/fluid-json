package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalDate


object LocalDateJSONCodec : AbstractJSONCodec<LocalDate, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in LocalDate>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				LocalDate.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalDate value: $raw")
			}
		}


	override fun encode(value: LocalDate, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
