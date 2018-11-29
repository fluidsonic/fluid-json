package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetTime


object OffsetTimeJSONCodec : AbstractJSONCodec<OffsetTime, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in OffsetTime>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				OffsetTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetTime value: $raw")
			}
		}


	override fun encode(value: OffsetTime, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
