package io.fluidsonic.json

import java.time.*


object LocalTimeJsonCodec : AbstractJsonCodec<LocalTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<LocalTime>) =
		readString().let { raw ->
			try {
				LocalTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("time in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: LocalTime) =
		writeString(value.toString())
}
