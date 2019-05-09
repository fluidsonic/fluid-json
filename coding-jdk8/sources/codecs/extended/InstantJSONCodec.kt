package com.github.fluidsonic.fluid.json

import java.time.*


object InstantJSONCodec : AbstractJSONCodec<Instant, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Instant>) =
		readString().let { raw ->
			try {
				Instant.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Instant) =
		writeString(value.toString())
}
