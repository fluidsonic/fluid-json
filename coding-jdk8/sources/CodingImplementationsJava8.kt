@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")

package com.github.fluidsonic.fluid.json.dynamic

import com.github.fluidsonic.fluid.json.*


@Suppress("unused")
internal open class CodingImplementationsJava8 : CodingImplementationsJava7() {

	override fun extendedCodecProviders() =
		listOf(
			DayOfWeekJSONCodec,
			DurationJSONCodec,
			InstantJSONCodec,
			LocalDateJSONCodec,
			LocalDateTimeJSONCodec,
			LocalTimeJSONCodec,
			MonthDayJSONCodec,
			MonthJSONCodec,
			OffsetDateTimeJSONCodec,
			OffsetTimeJSONCodec,
			PeriodJSONCodec,
			YearJSONCodec,
			YearMonthJSONCodec,
			ZonedDateTimeJSONCodec,
			ZoneIdJSONCodec,
			ZoneOffsetJSONCodec
		)
}
