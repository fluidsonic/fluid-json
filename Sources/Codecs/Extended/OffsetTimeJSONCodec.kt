package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.OffsetTime


object OffsetTimeJSONCodec : AbstractJSONCodec<OffsetTime, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in OffsetTime>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				OffsetTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse OffsetTime value: $raw")
			}
		}


	override fun encode(value: OffsetTime, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
