package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


// test data from http://pacificrim.wikia.com

internal object StandardDecoderTest {

	@Test
	fun testDecodesCompleteExample() {
		val input = """
				{
					"jaegers": [
						{
							"height":     76.2,
							"launchDate": "2019-11-02",
							"mark":       5,
							"name":       "Striker Eureka",
							"origin":     "Australia",
							"status":     "destroyed",
							"weight":     1850
						}
					],
					"kaijus": [
						{
							"breachDate": "2025-01-12",
							"category":   5,
							"height":     181.7,
							"name":       "Slattern",
							"origin":     "Anteverse",
							"status":     "deceased",
							"weight":     6750
						}
					]
				}
			"""

		val expectedOutput = Universe(
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

		val output = decode(
			input = input,
			codecProvider = JsonCodecProvider(
				JaegerCodec,
				KaijuCodec,
				YearMonthDayCodec,
				UniverseCodec,
				JsonCodecProvider.basic
			)
		) { readValueOfType<Universe>() }

		expect(output).toBe(expectedOutput)
	}


	@Test
	fun testCodecNotFound() {
		decode("{}") {
			class Unsupported

			try {
				readValueOfType<Unsupported>()
				throw AssertionError("an exception was expected")
			}
			catch (e: JsonException) {
				// good
			}
		}
	}


	@Test
	fun testPassingOfContextToCodec() {
		val context = TestCoderContext()

		decode(
			input = "\"test\"",
			codecProvider = JsonCodecProvider(ContextCheckingTestCodec(context)),
			context = context
		) { readValueOfType<String>() }

		decode(
			input = "\"test\"",
			codecProvider = JsonCodecProvider(ContextCheckingTestDecoderCodec(context)),
			context = context
		) { readValueOfType<String>() }
	}


	private inline fun <ReturnValue> decode(
		input: String,
		codecProvider: JsonCodecProvider<TestCoderContext> = JsonCodecProvider(),
		context: TestCoderContext = TestCoderContext(),
		block: JsonDecoder<TestCoderContext>.() -> ReturnValue
	) =
		StandardDecoder(
			codecProvider = codecProvider,
			context = context,
			source = JsonReader.build(input)
		).use(withTermination = false) { it.block() }
}
