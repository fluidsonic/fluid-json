package tests.basic

import kotlin.test.*


@JvmInline
value class Expect<Value>(private val actual: Any?) {

	fun toBe(expected: Value) {
		assertEquals(expected = expected, actual = actual)
	}
}


@Suppress("NOTHING_TO_INLINE")
inline fun <Value> expect(actual: Value): Expect<Value> =
	Expect(actual)
