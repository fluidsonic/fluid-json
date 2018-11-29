package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Instant


object InstantJSONCodec : AbstractJSONCodec<Instant, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Instant>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
			try {
				Instant.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Instant value: $raw")
			}
		}


	override fun encode(value: Instant, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(value.toString())
}
