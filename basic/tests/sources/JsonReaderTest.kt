package tests.basic

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import org.junit.jupiter.api.*


internal object JsonReaderTest {

	@Test
	fun testDefaultReadDelegations() {
		val reader = object : DummyJsonReader() {
			override fun readDouble() = 10.0
		}

		expect(reader.readFloat()).toBe(10.0f)
	}
}
