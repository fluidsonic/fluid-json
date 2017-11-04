package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Duration


object DurationJSONCodec : AbstractJSONCodec<Duration, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Duration>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readString().let { raw ->
			try {
				Duration.parse(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Cannot parse Duration value: $raw")
			}
		}


	override fun encode(value: Duration, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeString(value.toString())
}
