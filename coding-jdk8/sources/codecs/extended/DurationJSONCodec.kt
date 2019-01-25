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
				invalidValueError("duration in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Duration) =
		writeString(value.toString())
}
