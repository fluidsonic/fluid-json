package io.fluidsonic.json

import java.time.*


// TODO use Enum codec once implemented
object MonthJsonCodec : AbstractJsonCodec<Month, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Month>) =
		readString().let { raw ->
			when (raw) {
				"january" -> Month.JANUARY
				"february" -> Month.FEBRUARY
				"march" -> Month.MARCH
				"april" -> Month.APRIL
				"may" -> Month.MAY
				"june" -> Month.JUNE
				"july" -> Month.JULY
				"august" -> Month.AUGUST
				"september" -> Month.SEPTEMBER
				"october" -> Month.OCTOBER
				"november" -> Month.NOVEMBER
				"december" -> Month.DECEMBER
				else -> invalidValueError("month name of 'january' through 'december' expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Month) =
		writeString(when (value) {
			Month.JANUARY -> "january"
			Month.FEBRUARY -> "february"
			Month.MARCH -> "march"
			Month.APRIL -> "april"
			Month.MAY -> "may"
			Month.JUNE -> "june"
			Month.JULY -> "july"
			Month.AUGUST -> "august"
			Month.SEPTEMBER -> "september"
			Month.OCTOBER -> "october"
			Month.NOVEMBER -> "november"
			Month.DECEMBER -> "december"
		})
}
