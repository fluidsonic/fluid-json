package io.fluidsonic.json


/** Contains the default lists of [JsonCodecProvider]s for basic, extended, and non-recursive codec configurations. */
public object DefaultJsonCodecs {

	public val basic: List<JsonCodecProvider<JsonCodingContext>> =
		listOf(
			AnyJsonDecoderCodec,
			ArrayJsonCodec,
			BooleanArrayJsonCodec,
			BooleanJsonCodec,
			ByteArrayJsonCodec,
			ByteJsonCodec,
			CharJsonCodec,
			DoubleArrayJsonCodec,
			DoubleJsonCodec,
			FloatArrayJsonCodec,
			FloatJsonCodec,
			IntArrayJsonCodec,
			IntJsonCodec,
			ListJsonDecoderCodec,
			LongArrayJsonCodec,
			LongJsonCodec,
			MapJsonCodec,
			SequenceJsonCodec,
			SetJsonDecoderCodec,
			ShortArrayJsonCodec,
			ShortJsonCodec,
			StringJsonCodec,
			CollectionJsonCodec, // after subclasses
			IterableJsonEncoderCodec, // after subclasses
			NumberJsonCodec // after subclasses
		)


	public val extended: List<JsonCodecProvider<JsonCodingContext>> =
		listOf(
			// Ranges (specific to unspecific)
			CharRangeJsonCodec,
			IntRangeJsonCodec,
			LongRangeJsonCodec,
			ClosedRangeJsonCodec,
			// Enum
			EnumJsonCodecProvider(
				transformation = EnumJsonTransformation.ToString(case = EnumJsonTransformation.Case.lowerCamelCase)
			),
			// Temporal (Java Time)
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
			ZoneOffsetJsonCodec,
		)


	public val nonRecursive: List<JsonCodecProvider<JsonCodingContext>> =
		listOf(
			ArrayJsonCodec.nonRecursive,
			ListJsonDecoderCodec.nonRecursive,
			MapJsonCodec.nonRecursive,
			SequenceJsonCodec.nonRecursive,
			SetJsonDecoderCodec.nonRecursive,
			CollectionJsonCodec.nonRecursive, // after subclasses
			IterableJsonEncoderCodec.nonRecursive // after subclasses
		)
}
