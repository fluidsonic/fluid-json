package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*
import java.time.*


@Suppress("UNCHECKED_CAST")
internal object ExtendedCodecsTest {

	private val cases = listOf(
		TestCase("DayOfWeek", DayOfWeekJSONCodec, dayOfWeekData),
		TestCase("Duration", DurationJSONCodec, durationData),
		TestCase("Instant", InstantJSONCodec, instantData),
		TestCase("LocalDate", LocalDateJSONCodec, localDateData),
		TestCase("LocalDateTime", LocalDateTimeJSONCodec, localDateTimeData),
		TestCase("LocalTime", LocalTimeJSONCodec, localTimeData),
		TestCase("Month", MonthJSONCodec, monthData),
		TestCase("MonthDay", MonthDayJSONCodec, monthDayData),
		TestCase("OffsetDateTime", OffsetDateTimeJSONCodec, offsetDateTimeData),
		TestCase("OffsetTime", OffsetTimeJSONCodec, offsetTimeData),
		TestCase("Period", PeriodJSONCodec, periodData),
		TestCase("Year", YearJSONCodec, yearData),
		TestCase("YearMonth", YearMonthJSONCodec, yearMonthData),
		TestCase("ZonedDateTime", ZonedDateTimeJSONCodec, zonedDateTimeData),
		TestCase("ZoneId", ZoneIdJSONCodec, zoneIdData),
		TestCase("ZoneOffset", ZoneOffsetJSONCodec, zoneOffsetData)
	)


	private fun buildTests(case: TestCase<*>) = listOfNotNull(
		(case.codec as? JSONDecoderCodec<Any, JSONCodingContext>)?.let { decoderCodec ->
			DynamicTest.dynamicTest("is decodable") {
				try {
					case.data.testDecoding { decoderCodec.parse(it, type = case.type as JSONCodingType<Any>) }
				}
				catch (e: Throwable) {
					throw AssertionError("${decoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		},

		(case.codec as? JSONEncoderCodec<Any, JSONCodingContext>)?.let { encoderCodec ->
			DynamicTest.dynamicTest("is encodable") {
				try {
					case.data.testEncoding { encoderCodec.serialize(it) }
				}
				catch (e: Throwable) {
					throw AssertionError("${encoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		}
	)


	@TestFactory
	fun test() = cases
		.map { case ->
			DynamicContainer.dynamicContainer(
				case.name,
				buildTests(case = case)
			)
		}


	@TestFactory
	fun testDefaultCodecs(): List<DynamicTest> {
		val provider = JSONCodecProvider(DefaultJSONCodecs.extended)

		return listOf(
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
			.map {
				DynamicTest.dynamicTest("for ${it.simpleName}") {
					assert(provider.encoderCodecForClass(it)).notToBeNull {}
				}
			}
	}


	private class TestCase<Value : Any> private constructor(
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
				TestCase(name = name, codec = codec, type = jsonCodingType(), data = data)
		}
	}
}
