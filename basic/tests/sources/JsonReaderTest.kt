package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import org.junit.jupiter.api.*


internal object JsonReaderTest {

	@Test
	fun testDefaultReadDelegations() {
		val reader = object : DummyJsonReader() {
			override fun readDouble() = 10.0
		}

		assert(reader.readFloat()).toBe(10.0f)
	}
}
