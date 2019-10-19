package io.fluidsonic.json

import java.time.*


object LocalDateTimeJsonCodec : AbstractJsonCodec<LocalDateTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<LocalDateTime>) =
		readString().let { raw ->
			try {
				LocalDateTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date and time in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: LocalDateTime) =
		writeString(value.toString())
}
