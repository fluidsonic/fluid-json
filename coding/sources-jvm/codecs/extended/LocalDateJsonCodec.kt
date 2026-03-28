package io.fluidsonic.json

import java.time.*


public object LocalDateJsonCodec : AbstractJsonCodec<LocalDate, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<LocalDate>): LocalDate =
		readString().let { raw ->
			try {
				LocalDate.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: LocalDate): Unit =
		writeString(value.toString())
}
