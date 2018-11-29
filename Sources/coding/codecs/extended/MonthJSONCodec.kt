package com.github.fluidsonic.fluid.json

import java.time.Month


// TODO use Enum codec once implemented
object MonthJSONCodec : AbstractJSONCodec<Month, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Month>, decoder: JSONDecoder<JSONCodingContext>) =
		decoder.readString().let { raw ->
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
				else -> throw JSONException("Invalid Month value: $raw")
			}
		}


	override fun encode(value: Month, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeString(when (value) {
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
