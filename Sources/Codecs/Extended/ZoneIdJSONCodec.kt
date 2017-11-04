package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZoneId


object ZoneIdJSONCodec : AbstractJSONCodec<ZoneId, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in ZoneId>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				ZoneId.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZoneId value: $raw")
			}
		}


	override fun encode(value: ZoneId, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.id)
}
