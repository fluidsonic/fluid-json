package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*


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
					?: throw AssertionError("Output is null but expected $expectedOutput (${expectedOutput::class})")

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
				assert(encode(input)).toBe(expectedOutput)
			}
			catch (e: Throwable) {
				throw AssertionError("${e.message} - when encoding ${input::class}: $input").apply {
					stackTrace = e.stackTrace
				}
			}

		for (input in nonEncodable)
			try {
				encode(input)
				throw AssertionError("Encoding succeeded but should have failed when encoding ${input::class}: $input")
			}
			catch (e: JSONException) {
				// good
			}
	}


	private fun testEquals(actual: Any, expected: Any) {
		val isEqual = expected == actual
		if (!isEqual) {
			val printableActual = (actual as? Sequence<*>)?.toList() ?: actual
			val printableExpected = (expected as? Sequence<*>)?.toList() ?: expected

			throw AssertionError("$printableActual (${actual::class}) should equal $printableExpected (${expected::class})")
		}
	}
}
