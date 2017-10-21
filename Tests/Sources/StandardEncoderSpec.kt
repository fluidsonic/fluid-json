package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONWriter
import com.github.fluidsonic.fluid.json.StandardEncoder
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
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
				codecResolver = JSONCodecResolver.of(
					JaegerCodec,
					KaijuCodec,
					LocalDateCodec,
					UniverseCodec
				)
			) {
				writeEncodable(input)
			}

			output.should.equal(expectedOutput)
		}


		it("fails when a codec was no found") {
			encode {
				try {
					writeEncodable(object {})
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
				codecResolver = JSONCodecResolver.of(ContextCheckingTestCodec(context)),
				context = context
			) { writeEncodable("test") }

			encode(
				codecResolver = JSONCodecResolver.of(ContextCheckingTestEncoderCodec(context)),
				context = context
			) { writeEncodable("test") }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private inline fun encode(
	codecResolver: JSONCodecResolver<TestCoderContext> = JSONCodecResolver.of(),
	context: TestCoderContext = TestCoderContext(),
	body: JSONEncoder<TestCoderContext>.() -> Unit
): String {
	val writer = StringWriter()
	val encoder =
		StandardEncoder(
			codecResolver = codecResolver,
			context = context,
			destination = JSONWriter.with(writer)
		)

	encoder.use {
		encoder.body()
	}

	return writer.toString()
}
