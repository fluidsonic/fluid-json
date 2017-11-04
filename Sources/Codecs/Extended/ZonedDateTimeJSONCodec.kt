package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZonedDateTime


object ZonedDateTimeJSONCodec : AbstractJSONCodec<ZonedDateTime, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in ZonedDateTime>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				ZonedDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZonedDateTime value: $raw")
			}
		}


	override fun encode(value: ZonedDateTime, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
