package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Duration


object DurationJSONCodec : AbstractJSONCodec<Duration, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Duration>) =
		readString().let { raw ->
			try {
				Duration.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Duration value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Duration) =
		writeString(value.toString())
}
