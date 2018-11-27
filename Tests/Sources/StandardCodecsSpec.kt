package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object StandardCodecsSpec : Spek({

	describe("standard codecs") {
		listOf(
			// basic
			Test("Any", AnyJSONTestDecoderCodec, anyData),
			Test("Array", ArrayJSONTestCodec, arrayData),
			Test("Array (non-recursive)", ArrayJSONTestCodec.NonRecursive, arrayData),
			Test("Boolean", BooleanJSONCodec, booleanData),
			Test("BooleanArray", BooleanArrayJSONCodec, booleanArrayData),
			Test("Byte", ByteJSONCodec, byteData),
			Test("ByteArray", ByteArrayJSONCodec, byteArrayData),
			Test("Double", DoubleJSONCodec, doubleData),
			Test("DoubleArray", DoubleArrayJSONCodec, doubleArrayData),
			Test("Float", FloatJSONCodec, floatData),
			Test("FloatArray", FloatArrayJSONCodec, floatArrayData),
			Test("Int", IntJSONCodec, intData),
			Test("IntArray", IntArrayJSONCodec, intArrayData),
			Test("Iterable", IterableJSONEncoderTestCodec, iterableData),
			Test("Iterable (non-recursive)", IterableJSONEncoderTestCodec.NonRecursive, iterableData),
			Test("List", ListJSONTestDecoderCodec, listData),
			Test("List (non-recursive)", ListJSONTestDecoderCodec.NonRecursive, listData),
			Test("Long", LongJSONCodec, longData),
			Test("LongArray", LongArrayJSONCodec, longArrayData),
			Test("Map", MapJSONTestCodec, mapData),
			Test("Map (non-recursive)", MapJSONTestCodec.NonRecursive, mapData),
			Test("Map (non-String keys)", MapJSONTestCodec, mapDataWithNonStringKeys),
			Test("Number", NumberJSONCodec, numberData),
			Test("Sequence", SequenceJSONTestCodec, sequenceData),
			Test("Sequence (non-recursive)", SequenceJSONTestCodec.NonRecursive, sequenceData),
			Test("Short", ShortJSONCodec, shortData),
			Test("ShortArray", ShortArrayJSONCodec, shortArrayData),
			Test("String", StringJSONCodec, stringData),

			// extended
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
					val decoderCodec = test.codec as? JSONDecoderCodec<Any, JSONCoderContext>
					if (decoderCodec != null) {
						it("decodes a value") {
							try {
								test.data.testDecoding { decoderCodec.parse(it, type = test.type as JSONCodableType<Any>) }
							}
							catch (e: Throwable) {
								throw AssertionError("${decoderCodec::class.simpleName} (for ${test.type}): ${e.message}").apply {
									stackTrace = e.stackTrace
								}
							}
						}
					}

					val encoderCodec = test.codec as? JSONEncoderCodec<Any, JSONCoderContext>
					if (encoderCodec != null) {
						it("encodes a value") {
							try {
								test.data.testEncoding(encoderCodec::serialize)
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


		it("Map maintains entry order") {
			MapJSONTestCodec.parse("""{ "3": 3, "1": 1, "2": 2, "0": 0 }""", jsonCodableType())
				.entries.toList()
				.should.equal(mapOf("3" to 3, "1" to 1, "2" to 2, "0" to 0).entries.toList())
		}
	}
}) {

	private class Test<Value : Any> private constructor(
		val name: String,
		val codec: JSONCodecProvider<JSONCoderContext>,
		val type: JSONCodableType<Value>,
		val data: TestData<Value>
	) {

		companion object {

			inline operator fun <reified Value : Any> invoke(
				name: String,
				codec: JSONCodecProvider<JSONCoderContext>,
				data: TestData<Value>
			) =
				Test(name = name, codec = codec, type = jsonCodableType(), data = data)
		}
	}
}
