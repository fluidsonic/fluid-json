package com.github.fluidsonic.fluid.json


object DefaultJSONCodecs {

	val basic = listOf(
		AnyJSONDecoderCodec,
		ArrayJSONCodec,
		BooleanArrayJSONCodec,
		BooleanJSONCodec,
		ByteArrayJSONCodec,
		ByteJSONCodec,
		DoubleArrayJSONCodec,
		DoubleJSONCodec,
		FloatArrayJSONCodec,
		FloatJSONCodec,
		IntArrayJSONCodec,
		IntJSONCodec,
		ListJSONDecoderCodec,
		LongArrayJSONCodec,
		LongJSONCodec,
		MapJSONCodec,
		SequenceJSONCodec,
		ShortArrayJSONCodec,
		ShortJSONCodec,
		StringJSONCodec,
		IterableJSONEncoderCodec, // after subclasses
		NumberJSONCodec // after subclasses
	)


	val extended = listOf(
		DayOfWeekJSONCodec,
		DurationJSONCodec,
		InstantJSONCodec,
		LocalDateJSONCodec,
		LocalDateTimeJSONCodec,
		LocalTimeJSONCodec,
		MonthDayJSONCodec,
		MonthJSONCodec,
		OffsetTimeJSONCodec,
		OffsetTimeJSONCodec,
		PeriodJSONCodec,
		YearJSONCodec,
		YearMonthJSONCodec,
		ZonedDateTimeJSONCodec,
		ZoneIdJSONCodec,
		ZoneOffsetJSONCodec
	)


	val nonRecursive = listOf(
		ArrayJSONCodec.nonRecursive,
		ListJSONDecoderCodec.nonRecursive,
		MapJSONCodec.nonRecursive,
		SequenceJSONCodec.nonRecursive,
		IterableJSONEncoderCodec.nonRecursive // after subclasses
	)
}
