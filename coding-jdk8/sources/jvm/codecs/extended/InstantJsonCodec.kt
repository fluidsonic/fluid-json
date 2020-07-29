package io.fluidsonic.json

import java.time.*


public object InstantJsonCodec : AbstractJsonCodec<Instant, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Instant>): Instant =
		readString().let { raw ->
			try {
				Instant.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Instant): Unit =
		writeString(value.toString())
}
