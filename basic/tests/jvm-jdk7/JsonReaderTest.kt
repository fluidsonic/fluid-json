package tests.basic

import kotlin.test.*


class JsonReaderTest {

	@Test
	fun testDefaultReadDelegations() {
		val reader = object : DummyJsonReader() {
			override fun readDouble() = 10.0
		}

		expect(reader.readFloat()).toBe(10.0f)
	}
}
