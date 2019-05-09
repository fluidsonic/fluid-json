package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JSONWriterTest {

	@Test
	fun testWithErrorChecking() {
		val writer = JSONWriter.build(StringWriter())
		assert(writer.isErrored).toBe(false)
		assert(writer.withErrorChecking { "test" }).toBe("test")
		assert(writer.isErrored).toBe(false)

		try {
			if (0.toBigDecimal() == 0.toBigDecimal()) writer.withErrorChecking { throw RuntimeException() }
			throw AssertionError(".withErrorChecking() should not consume exception")
		}
		catch (e: Throwable) {
			assert(writer.isErrored).toBe(true)
		}

		try {
			writer.withErrorChecking { }
			throw AssertionError(".withErrorChecking() throw when errored")
		}
		catch (e: JSONException) {
			assert(writer.isErrored).toBe(true)
		}
	}


	@Test
	fun testDefaultWriteDelegations() {
		var doubleValue: Double? = null
		var longValue: Long? = null
		var stringValue: String? = null

		val writer = object : DummyJSONWriter() {

			override fun writeDouble(value: Double) {
				doubleValue = value
			}

			override fun writeLong(value: Long) {
				longValue = value
			}

			override fun writeString(value: String) {
				stringValue = value
			}
		}

		writer.writeByte(1)
		assert(longValue).toBe(1L)

		writer.writeFloat(2.0f)
		assert(doubleValue).toBe(2.0)

		writer.writeInt(3)
		assert(longValue).toBe(3)

		writer.writeMapKey("4")
		assert(stringValue).toBe("4")

		writer.writeShort(5)
		assert(longValue).toBe(5)
	}
}
