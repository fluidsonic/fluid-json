package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetDateTime


object OffsetDateTimeJSONCodec : AbstractJSONCodec<OffsetDateTime, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in OffsetDateTime>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetDateTime value: $raw")
			}
		}


	override fun encode(value: OffsetDateTime, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
