package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONReader
import com.github.fluidsonic.fluid.json.StandardDecoder
import com.github.fluidsonic.fluid.json.readDecodable
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.time.LocalDate


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
						launchDate = LocalDate.of(2019, 11, 2),
						mark = 5,
						name = "Striker Eureka",
						origin = "Australia",
						status = Jaeger.Status.destroyed,
						weight = 1_850.0
					))
				,
				kaijus = listOf(
					Kaiju(
						breachDate = LocalDate.of(2025, 1, 12),
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
				codecResolver = JSONCodecResolver.of(
					JaegerCodec,
					KaijuCodec,
					LocalDateCodec,
					UniverseCodec
				)
			) { readDecodable<Universe>() }

			output.should.equal(expectedOutput)
		}


		it("fails when a codec was no found") {
			decode("{}") {
				try {
					readDecodableOfClass(object {}::class.java)
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
				codecResolver = JSONCodecResolver.of(ContextCheckingTestCodec(context)),
				context = context
			) { readDecodable<String>() }

			decode(
				input = "\"test\"",
				codecResolver = JSONCodecResolver.of(ContextCheckingTestDecoderCodec(context)),
				context = context
			) { readDecodable<String>() }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun <ReturnValue> decode(
	input: String,
	codecResolver: JSONCodecResolver<TestCoderContext> = JSONCodecResolver.of(),
	context: TestCoderContext = TestCoderContext(),
	body: JSONDecoder<TestCoderContext>.() -> ReturnValue
) =
	StandardDecoder(
		codecResolver = codecResolver,
		context = context,
		source = JSONReader.with(input)
	).let { it.use { it.body() } }
