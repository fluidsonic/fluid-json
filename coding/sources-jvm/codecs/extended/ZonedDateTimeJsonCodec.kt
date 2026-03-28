package io.fluidsonic.json

import java.time.*


public object ZonedDateTimeJsonCodec : AbstractJsonCodec<ZonedDateTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<ZonedDateTime>): ZonedDateTime =
		readString().let { raw ->
			try {
				ZonedDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: ZonedDateTime): Unit =
		writeString(value.toString())
}
