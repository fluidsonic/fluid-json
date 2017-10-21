package tests

import com.winterbe.expekt.should


internal class TestData<out Value : Any>(
	val symmetric: Map<out Value, String> = emptyMap(),
	val decodableOnly: Map<String, Value> = emptyMap(),
	val encodableOnly: Map<out Value, String> = emptyMap()
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

			for (element in elements) {
				symmetric += element.symmetric
				decodableOnly += element.decodableOnly
				encodableOnly += element.encodableOnly
			}

			return TestData(
				symmetric = symmetric,
				decodableOnly = decodableOnly,
				encodableOnly = encodableOnly
			)
		}


		fun <Value : Any> ofEncodable(vararg elements: TestData<Value>): TestData<Value> {
			val encodableOnly = mutableMapOf<Value, String>()
			for (element in elements) {
				encodableOnly += element.symmetric
				encodableOnly += element.encodableOnly
			}

			return TestData(encodableOnly = encodableOnly)
		}
	}
}
