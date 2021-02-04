package io.fluidsonic.json

import java.time.*


public object DurationJsonCodec : AbstractJsonCodec<Duration, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Duration>): Duration =
		readString().let { raw ->
			try {
				Duration.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("duration in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Duration): Unit =
		writeString(value.toString())
}
