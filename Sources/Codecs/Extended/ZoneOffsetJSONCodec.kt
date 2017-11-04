package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.ZoneOffset


object ZoneOffsetJSONCodec : AbstractJSONCodec<ZoneOffset, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in ZoneOffset>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				ZoneOffset.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse ZoneOffset value: $raw")
			}
		}


	override fun encode(value: ZoneOffset, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.id)
}
