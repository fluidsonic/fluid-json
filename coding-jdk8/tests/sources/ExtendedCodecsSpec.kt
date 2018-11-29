package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime


@Suppress("UNCHECKED_CAST")
internal object ExtendedCodecsSpec : Spek({

	describe("extended codecs") {
		listOf(
			Test("DayOfWeek", DayOfWeekJSONCodec, dayOfWeekData),
			Test("Duration", DurationJSONCodec, durationData),
			Test("Instant", InstantJSONCodec, instantData),
			Test("LocalDate", LocalDateJSONCodec, localDateData),
			Test("LocalDateTime", LocalDateTimeJSONCodec, localDateTimeData),
			Test("LocalTime", LocalTimeJSONCodec, localTimeData),
			Test("Month", MonthJSONCodec, monthData),
			Test("MonthDay", MonthDayJSONCodec, monthDayData),
			Test("OffsetDateTime", OffsetDateTimeJSONCodec, offsetDateTimeData),
			Test("OffsetTime", OffsetTimeJSONCodec, offsetTimeData),
			Test("Period", PeriodJSONCodec, periodData),
			Test("Year", YearJSONCodec, yearData),
			Test("YearMonth", YearMonthJSONCodec, yearMonthData),
			Test("ZonedDateTime", ZonedDateTimeJSONCodec, zonedDateTimeData),
			Test("ZoneId", ZoneIdJSONCodec, zoneIdData),
			Test("ZoneOffset", ZoneOffsetJSONCodec, zoneOffsetData)
		)
			.forEach { test ->
				describe(test.name) {
					val decoderCodec = test.codec as? JSONDecoderCodec<Any, JSONCodingContext>
					if (decoderCodec != null) {
						it("decodes a value") {
							try {
								test.data.testDecoding { decoderCodec.parse(it, type = test.type as JSONCodingType<Any>) }
							}
							catch (e: Throwable) {
								throw AssertionError("${decoderCodec::class.simpleName} (for ${test.type}): ${e.message}").apply {
									stackTrace = e.stackTrace
								}
							}
						}
					}

					val encoderCodec = test.codec as? JSONEncoderCodec<Any, JSONCodingContext>
					if (encoderCodec != null) {
						it("encodes a value") {
							try {
								test.data.testEncoding { encoderCodec.serialize(it) }
							}
							catch (e: Throwable) {
								throw AssertionError("${encoderCodec::class.simpleName} (for ${test.type}): ${e.message}").apply {
									stackTrace = e.stackTrace
								}
							}
						}
					}
				}
			}

		describe("are provided through dynamic implementation lookup") {
			val provider = JSONCodecProvider.of(DefaultJSONCodecs.extended)

			listOf(
				DayOfWeek::class,
				Duration::class,
				Instant::class,
				LocalDate::class,
				LocalDateTime::class,
				LocalTime::class,
				Month::class,
				MonthDay::class,
				OffsetDateTime::class,
				OffsetTime::class,
				Period::class,
				Year::class,
				YearMonth::class,
				ZonedDateTime::class,
				ZoneId::class,
				ZoneOffset::class
			)
				.forEach {
					it("for ${it.simpleName}") {
						provider.encoderCodecForClass(it).should.not.be.`null`
					}
				}
		}
	}
}) {

	private class Test<Value : Any> private constructor(
		val name: String,
		val codec: JSONCodecProvider<JSONCodingContext>,
		val type: JSONCodingType<Value>,
		val data: TestData<Value>
	) {

		companion object {

			inline operator fun <reified Value : Any> invoke(
				name: String,
				codec: JSONCodecProvider<JSONCodingContext>,
				data: TestData<Value>
			) =
				Test(name = name, codec = codec, type = jsonCodingType(), data = data)
		}
	}
}
