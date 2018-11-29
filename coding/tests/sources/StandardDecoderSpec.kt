package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


// test data from http://pacificrim.wikia.com

internal object StandardDecoderSpec : Spek({

	describe("StandardDecoder") {

		it("decodes complete example") {
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
				codecProvider = JSONCodecProvider.of(
					JaegerCodec,
					KaijuCodec,
					YearMonthDayCodec,
					UniverseCodec
				)
			) { readValueOfType<Universe>() }

			output.should.equal(expectedOutput)
		}


		it("fails when a codec was no found") {
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


		it("provides codecs with the context") {
			val context = TestCoderContext()

			decode(
				input = "\"test\"",
				codecProvider = JSONCodecProvider.of(ContextCheckingTestCodec(context)),
				context = context
			) { readValueOfType<String>() }

			decode(
				input = "\"test\"",
				codecProvider = JSONCodecProvider.of(ContextCheckingTestDecoderCodec(context)),
				context = context
			) { readValueOfType<String>() }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun <ReturnValue> decode(
	input: String,
	codecProvider: JSONCodecProvider<TestCoderContext> = JSONCodecProvider.of(),
	context: TestCoderContext = TestCoderContext(),
	block: JSONDecoder<TestCoderContext>.() -> ReturnValue
) =
	StandardDecoder(
		codecProvider = codecProvider,
		context = context,
		source = JSONReader.build(input)
	).use(withTermination = false) { it.block() }
