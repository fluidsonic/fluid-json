package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


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
			codecProvider = JSONCodecProvider(
				JaegerCodec,
				KaijuCodec,
				YearMonthDayCodec,
				UniverseCodec,
				JSONCodecProvider.basic
			)
		) { readValueOfType<Universe>() }

		assert(output).toBe(expectedOutput)
	}


	@Test
	fun testCodecNotFound() {
		decode("{}") {
			class Unsupported

			try {
				readValueOfType<Unsupported>()
				throw AssertionError("an exception was expected")
			}
			catch (e: JSONException) {
				// good
			}
		}
	}


	@Test
	fun testPassingOfContextToCodec() {
		val context = TestCoderContext()

		decode(
			input = "\"test\"",
			codecProvider = JSONCodecProvider(ContextCheckingTestCodec(context)),
			context = context
		) { readValueOfType<String>() }

		decode(
			input = "\"test\"",
			codecProvider = JSONCodecProvider(ContextCheckingTestDecoderCodec(context)),
			context = context
		) { readValueOfType<String>() }
	}


	private inline fun <ReturnValue> decode(
		input: String,
		codecProvider: JSONCodecProvider<TestCoderContext> = JSONCodecProvider(),
		context: TestCoderContext = TestCoderContext(),
		block: JSONDecoder<TestCoderContext>.() -> ReturnValue
	) =
		StandardDecoder(
			codecProvider = codecProvider,
			context = context,
			source = JSONReader.build(input)
		).use(withTermination = false) { it.block() }
}
