package io.fluidsonic.json

import java.time.*


object OffsetDateTimeJsonCodec : AbstractJsonCodec<OffsetDateTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<OffsetDateTime>) =
		readString().let { raw ->
			try {
				OffsetDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: OffsetDateTime) =
		writeString(value.toString())
}
