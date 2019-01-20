package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Instant


object InstantJSONCodec : AbstractJSONCodec<Instant, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Instant>) =
		readString().let { raw ->
			try {
				Instant.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Instant value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Instant) =
		writeString(value.toString())
}
