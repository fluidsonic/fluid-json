package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.LocalTime


object LocalTimeJSONCodec : AbstractJSONCodec<LocalTime, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in LocalTime>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				LocalTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse LocalTime value: $raw")
			}
		}


	override fun encode(value: LocalTime, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
