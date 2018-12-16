package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import org.junit.jupiter.api.Test


internal object JSONReaderTest {

	@Test
	fun testDefaultReadDelegations() {
		val reader = object : DummyJSONReader() {
			override fun readDouble() = 10.0
		}

		assert(reader.readFloat()).toBe(10.0f)
	}
}
