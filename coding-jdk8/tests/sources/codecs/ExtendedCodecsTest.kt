package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.time.*


@Suppress("UNCHECKED_CAST")
internal object ExtendedCodecsTest {

	private val cases = listOf(
		TestCase("DayOfWeek", DayOfWeekJsonCodec, dayOfWeekData),
		TestCase("Duration", DurationJsonCodec, durationData),
		TestCase("Instant", InstantJsonCodec, instantData),
		TestCase("LocalDate", LocalDateJsonCodec, localDateData),
		TestCase("LocalDateTime", LocalDateTimeJsonCodec, localDateTimeData),
		TestCase("LocalTime", LocalTimeJsonCodec, localTimeData),
		TestCase("Month", MonthJsonCodec, monthData),
		TestCase("MonthDay", MonthDayJsonCodec, monthDayData),
		TestCase("OffsetDateTime", OffsetDateTimeJsonCodec, offsetDateTimeData),
		TestCase("OffsetTime", OffsetTimeJsonCodec, offsetTimeData),
		TestCase("Period", PeriodJsonCodec, periodData),
		TestCase("Year", YearJsonCodec, yearData),
		TestCase("YearMonth", YearMonthJsonCodec, yearMonthData),
		TestCase("ZonedDateTime", ZonedDateTimeJsonCodec, zonedDateTimeData),
		TestCase("ZoneId", ZoneIdJsonCodec, zoneIdData),
		TestCase("ZoneOffset", ZoneOffsetJsonCodec, zoneOffsetData)
	)


	private fun buildTests(case: TestCase<*>) = listOfNotNull(
		(case.codec as? JsonDecoderCodec<Any, JsonCodingContext>)?.let { decoderCodec ->
			DynamicTest.dynamicTest("is decodable") {
				try {
					case.data.testDecoding { decoderCodec.parse(it, type = case.type as JsonCodingType<Any>) }
				}
				catch (e: Throwable) {
					throw AssertionError("${decoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		},

		(case.codec as? JsonEncoderCodec<Any, JsonCodingContext>)?.let { encoderCodec ->
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
		val provider = JsonCodecProvider(DefaultJsonCodecs.extended)

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
					expect(provider.encoderCodecForClass(it)).notToBeNull()
				}
			}
	}


	private class TestCase<Value : Any> private constructor(
		val name: String,
		val codec: JsonCodecProvider<JsonCodingContext>,
		val type: JsonCodingType<Value>,
		val data: TestData<Value>
	) {

		companion object {

			inline operator fun <reified Value : Any> invoke(
				name: String,
				codec: JsonCodecProvider<JsonCodingContext>,
				data: TestData<Value>
			) =
				TestCase(name = name, codec = codec, type = jsonCodingType(), data = data)
		}
	}
}
