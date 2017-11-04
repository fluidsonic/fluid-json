package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Instant


object InstantJSONCodec : AbstractJSONCodec<Instant, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Instant>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				Instant.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Instant value: $raw")
			}
		}


	override fun encode(value: Instant, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
