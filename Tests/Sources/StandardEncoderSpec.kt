package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.StringWriter
import java.time.LocalDate


// test data from http://pacificrim.wikia.com

internal object StandardEncoderSpec : Spek({

	describe("StandardEncoder") {

		it("encodes complete example") {
			val input = Universe(
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

			val expectedOutput = """{"jaegers":[{"height":76.2,"launchDate":"2019-11-02","mark":5,"name":"Striker Eureka","origin":"Australia","status":"destroyed","weight":1850.0}],"kaijus":[{"breachDate":"2025-01-12","category":5,"height":181.7,"name":"Slattern","origin":"Anteverse","status":"deceased","weight":6750.0}]}"""

			val output = encode(
				codecProvider = JSONCodecProvider.of(
					JaegerCodec,
					KaijuCodec,
					LocalDateCodec,
					UniverseCodec
				)
			) {
				writeValue(input)
			}

			output.should.equal(expectedOutput)
		}

		it("fails when a codec was no found") {
			encode {
				try {
					writeValue(object {})
					throw AssertionError("an exception was expected")
				}
				catch (e: JSONException) {
					// good
				}
			}
		}

		it("provides codecs with the context") {
			val context = TestCoderContext()

			encode(
				codecProvider = JSONCodecProvider.of(ContextCheckingTestCodec(context)),
				context = context
			) { writeValue("test") }

			encode(
				codecProvider = JSONCodecProvider.of(ContextCheckingTestEncoderCodec(context)),
				context = context
			) { writeValue("test") }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun encode(
	codecProvider: JSONCodecProvider<TestCoderContext> = JSONCodecProvider.of(),
	context: TestCoderContext = TestCoderContext(),
	body: JSONEncoder<TestCoderContext>.() -> Unit
): String {
	val writer = StringWriter()
	val encoder =
		StandardEncoder(
			codecProvider = codecProvider,
			context = context,
			destination = JSONWriter.build(writer)
		)

	encoder.use {
		encoder.body()
	}

	return writer.toString()
}
