package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetTime


object OffsetTimeJSONCodec : AbstractJSONCodec<OffsetTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in OffsetTime>) =
		readString().let { raw ->
			try {
				OffsetTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetTime value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: OffsetTime) =
		writeString(value.toString())
}
