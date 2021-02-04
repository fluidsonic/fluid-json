package io.fluidsonic.json

import java.time.*


public object LocalDateTimeJsonCodec : AbstractJsonCodec<LocalDateTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<LocalDateTime>): LocalDateTime =
		readString().let { raw ->
			try {
				LocalDateTime.parse(raw)
			}
			catch (e: DateTimeException) {
				invalidValueError("date and time in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: LocalDateTime): Unit =
		writeString(value.toString())
}
