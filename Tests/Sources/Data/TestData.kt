package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.winterbe.expekt.should


internal class TestData<out Value : Any>(
	val symmetric: Map<out Value, String> = emptyMap(),
	val decodableOnly: Map<String, Value> = emptyMap(),
	val encodableOnly: Map<out Value, String> = emptyMap(),
	val nonEncodable: Set<Value> = emptySet()
) {

	fun testDecoding(decode: (input: String) -> Any?) {
		for ((expectedOutput, input) in symmetric.toList() + decodableOnly.map { it.value to it.key })
			try {
				val output = decode(input)
					?: throw AssertionError("Output is null but expected $expectedOutput (${expectedOutput::class.java})")

				testEquals(output, expectedOutput)
			}
			catch (e: Throwable) {
				throw AssertionError("${e.message} - when decoding: $input").apply {
					stackTrace = e.stackTrace
				}
			}
	}


	fun testEncoding(encode: (input: Value) -> String) {
		for ((input, expectedOutput) in symmetric.toList() + encodableOnly.toList())
			try {
				encode(input).should.equal(expectedOutput)
			}
			catch (e: Throwable) {
				throw AssertionError("${e.message} - when encoding ${input::class.java}: $input").apply {
					stackTrace = e.stackTrace
				}
			}

		for (input in nonEncodable)
			try {
				encode(input)
				throw AssertionError("Encoding succeeded but should have failed when encoding ${input::class.java}: $input")
			}
			catch (e: JSONException) {
				// good
			}
	}


	private fun testEquals(actual: Any, expected: Any) {
		val isEqual = if (expected is EquatableSequence<*>) expected == actual else actual == expected
		if (!isEqual) {
			val printableActual = (actual as? Sequence<*>)?.toList() ?: actual
			val printableExpected = (expected as? Sequence<*>)?.toList() ?: expected

			throw AssertionError("$printableActual (${actual::class.java}) should equal $printableExpected (${expected::class.java})")
		}
	}


	companion object {

		fun <Value : Any> of(vararg elements: TestData<Value>): TestData<Value> {
			val symmetric = mutableMapOf<Value, String>()
			val decodableOnly = mutableMapOf<String, Value>()
			val encodableOnly = mutableMapOf<Value, String>()
			val nonEncodable = mutableSetOf<Value>()

			for (element in elements) {
				symmetric += element.symmetric
				decodableOnly += element.decodableOnly
				encodableOnly += element.encodableOnly
				nonEncodable += element.nonEncodable
			}

			return TestData(
				symmetric = symmetric,
				decodableOnly = decodableOnly,
				encodableOnly = encodableOnly,
				nonEncodable = nonEncodable
			)
		}


		fun <Value : Any> ofEncodable(vararg elements: TestData<Value>): TestData<Value> {
			val encodableOnly = mutableMapOf<Value, String>()
			val nonEncodable = mutableSetOf<Value>()

			for (element in elements) {
				encodableOnly += element.symmetric
				encodableOnly += element.encodableOnly
				nonEncodable += element.nonEncodable
			}

			return TestData(
				encodableOnly = encodableOnly,
				nonEncodable = nonEncodable
			)
		}
	}
}
