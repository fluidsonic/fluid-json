package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZoneOffset


object ZoneOffsetJSONCodec : AbstractJSONCodec<ZoneOffset, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in ZoneOffset>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				ZoneOffset.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZoneOffset value: $raw")
			}
		}


	override fun encode(value: ZoneOffset, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.id)
}
