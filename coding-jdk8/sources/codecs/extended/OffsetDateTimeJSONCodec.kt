package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetDateTime


object OffsetDateTimeJSONCodec : AbstractJSONCodec<OffsetDateTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in OffsetDateTime>) =
		readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetDateTime value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: OffsetDateTime) =
		writeString(value.toString())
}
