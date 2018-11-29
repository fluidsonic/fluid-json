package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZoneId


object ZoneIdJSONCodec : AbstractJSONCodec<ZoneId, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in ZoneId>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				ZoneId.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZoneId value: $raw")
			}
		}


	override fun encode(value: ZoneId, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.id)
}
