package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZonedDateTime


object ZonedDateTimeJSONCodec : AbstractJSONCodec<ZonedDateTime, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in ZonedDateTime>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				ZonedDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZonedDateTime value: $raw")
			}
		}


	override fun encode(value: ZonedDateTime, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
