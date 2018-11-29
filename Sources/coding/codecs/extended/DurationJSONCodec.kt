package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Duration


object DurationJSONCodec : AbstractJSONCodec<Duration, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Duration>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				Duration.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Duration value: $raw")
			}
		}


	override fun encode(value: Duration, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
