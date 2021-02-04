package io.fluidsonic.json

import java.time.*


public object OffsetDateTimeJsonCodec : AbstractJsonCodec<OffsetDateTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<OffsetDateTime>): OffsetDateTime =
		readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: OffsetDateTime): Unit =
		writeString(value.toString())
}
