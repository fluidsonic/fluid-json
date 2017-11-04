package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.MonthDay


object MonthDayJSONCodec : AbstractJSONCodec<MonthDay, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in MonthDay>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				MonthDay.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse MonthDay value: $raw")
			}
		}


	override fun encode(value: MonthDay, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
