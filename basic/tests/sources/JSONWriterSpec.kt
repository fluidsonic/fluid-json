package tests.basic

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.IOException
import java.io.StringWriter


internal object JSONWriterSpec : Spek({

	describe("JSONWriter") {

		it(".withErrorChecking()") {
			val writer = JSONWriter.build(StringWriter())
			writer.isErrored.should.be.`false`
			writer.withErrorChecking { "test" }.should.equal("test")
			writer.isErrored.should.be.`false`

			try {
				if (0.toBigDecimal() == 0.toBigDecimal()) writer.withErrorChecking { throw RuntimeException() }
				throw AssertionError(".withErrorChecking() should not consume exception")
			}
			catch (e: Throwable) {
				writer.isErrored.should.be.`true`
			}

			try {
				writer.withErrorChecking { }
				throw AssertionError(".withErrorChecking() throw when errored")
			}
			catch (e: IOException) {
				writer.isErrored.should.be.`true`
			}
		}

		it(".write*() default implementations") {
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
			longValue.should.equal(1)

			writer.writeFloat(2.0f)
			doubleValue.should.equal(2.0)

			writer.writeInt(3)
			longValue.should.equal(3)

			writer.writeMapKey("4")
			stringValue.should.equal("4")

			writer.writeShort(5)
			longValue.should.equal(5)
		}
	}
})
