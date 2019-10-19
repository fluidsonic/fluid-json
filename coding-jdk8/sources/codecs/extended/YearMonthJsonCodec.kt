package io.fluidsonic.json

import java.time.*


object YearMonthJsonCodec : AbstractJsonCodec<YearMonth, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<YearMonth>) =
		readString().let { raw ->
			try {
				YearMonth.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("date without day in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: YearMonth) =
		writeString(value.toString())
}
