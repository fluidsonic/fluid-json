package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Period


object PeriodJSONCodec : AbstractJSONCodec<Period, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Period>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				Period.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Period value: $raw")
			}
		}


	override fun encode(value: Period, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
