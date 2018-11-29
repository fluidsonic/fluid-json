package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetDateTime


object OffsetDateTimeJSONCodec : AbstractJSONCodec<OffsetDateTime, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in OffsetDateTime>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetDateTime value: $raw")
			}
		}


	override fun encode(value: OffsetDateTime, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
