@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")

package io.fluidsonic.json.dynamic

import io.fluidsonic.json.*


@Suppress("unused")
internal open class CodingImplementationsJava8 : CodingImplementationsJava7() {

	override fun extendedCodecProviders() =
		super.extendedCodecProviders() + listOf(
			DayOfWeekJsonCodec,
			DurationJsonCodec,
			InstantJsonCodec,
			LocalDateJsonCodec,
			LocalDateTimeJsonCodec,
			LocalTimeJsonCodec,
			MonthDayJsonCodec,
			MonthJsonCodec,
			OffsetDateTimeJsonCodec,
			OffsetTimeJsonCodec,
			PeriodJsonCodec,
			YearJsonCodec,
			YearMonthJsonCodec,
			ZonedDateTimeJsonCodec,
			ZoneIdJsonCodec,
			ZoneOffsetJsonCodec
		)
}
