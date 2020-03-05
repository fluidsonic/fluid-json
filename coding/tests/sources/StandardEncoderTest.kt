package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*


// test data from http://pacificrim.wikia.com

internal object StandardEncoderTest {

	@Test
	fun testEncodesCompleteExample() {
		val input = Universe(
			jaegers = listOf(
				Jaeger(
					height = 76.2,
					launchDate = YearMonthDay(2019, 11, 2),
					mark = 5,
					name = "Striker Eureka",
					origin = "Australia",
					status = Jaeger.Status.destroyed,
					weight = 1_850.0
				))
			,
			kaijus = listOf(
				Kaiju(
					breachDate = YearMonthDay(2025, 1, 12),
					category = 5,
					height = 181.7,
					name = "Slattern",
					origin = "Anteverse",
					status = Kaiju.Status.deceased,
					weight = 6_750.0
				)
			)
		)

		val expectedOutput = """{"jaegers":[{"height":76.2,"launchDate":"2019-11-02","mark":5,"name":"Striker Eureka","origin":"Australia","status":"destroyed","weight":1850.0}],"kaijus":[{"breachDate":"2025-01-12","category":5,"height":181.7,"name":"Slattern","origin":"Anteverse","status":"deceased","weight":6750.0}]}"""

		val output = encode(
			codecProvider = JsonCodecProvider(
				JaegerCodec,
				KaijuCodec,
				YearMonthDayCodec,
				UniverseCodec,
				JsonCodecProvider.basic
			)
		) {
			writeValue(input)
		}

		expect(output).toBe(expectedOutput)
	}


	@Test
	fun testCodecNotFound() {
		encode {
			try {
				writeValue(object {})
				throw AssertionError("an exception was expected")
			}
			catch (e: JsonException) {
				// good
			}
		}
	}


	@Test
	fun testPassesContextToCodec() {
		val context = TestCoderContext()

		encode(
			codecProvider = JsonCodecProvider(ContextCheckingTestCodec(context)),
			context = context
		) { writeValue("test") }

		encode(
			codecProvider = JsonCodecProvider(ContextCheckingTestEncoderCodec(context)),
			context = context
		) { writeValue("test") }
	}


	private inline fun encode(
		codecProvider: JsonCodecProvider<TestCoderContext> = JsonCodecProvider(),
		context: TestCoderContext = TestCoderContext(),
		block: JsonEncoder<TestCoderContext>.() -> Unit
	): String {
		val writer = StringWriter()

		StandardEncoder(
			codecProvider = codecProvider,
			context = context,
			destination = JsonWriter.build(writer)
		)
			.use(withTermination = false) {
				it.block()
			}

		return writer.toString()
	}
}
