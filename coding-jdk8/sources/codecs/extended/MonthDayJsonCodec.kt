package io.fluidsonic.json

import java.time.*


object MonthDayJsonCodec : AbstractJsonCodec<MonthDay, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<MonthDay>) =
		readString().let { raw ->
			try {
				MonthDay.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date without year in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: MonthDay) =
		writeString(value.toString())
}
