package tests.basic

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*


internal object JsonWriterTest {

	@Test
	fun testWithErrorChecking() {
		val writer = JsonWriter.build(StringWriter())
		expect(writer.isErrored).toBe(false)
		expect(writer.withErrorChecking { "test" }).toBe("test")
		expect(writer.isErrored).toBe(false)

		try {
			if (0.toBigDecimal() == 0.toBigDecimal()) writer.withErrorChecking { throw RuntimeException() }
			throw AssertionError(".withErrorChecking() should not consume exception")
		}
		catch (e: Throwable) {
			expect(writer.isErrored).toBe(true)
		}

		try {
			writer.withErrorChecking { }
			throw AssertionError(".withErrorChecking() throw when errored")
		}
		catch (e: JsonException) {
			expect(writer.isErrored).toBe(true)
		}
	}


	@Test
	fun testDefaultWriteDelegations() {
		var doubleValue: Double? = null
		var longValue: Long? = null
		var stringValue: String? = null

		val writer = object : DummyJsonWriter() {

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
		expect(longValue).toBe(1L)

		writer.writeFloat(2.0f)
		expect(doubleValue).toBe(2.0)

		writer.writeInt(3)
		expect(longValue).toBe(3)

		writer.writeMapKey("4")
		expect(stringValue).toBe("4")

		writer.writeShort(5)
		expect(longValue).toBe(5)
	}
}
