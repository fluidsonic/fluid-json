package tests.basic

import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test
import java.io.IOException
import java.io.StringWriter


internal object StandardWriterRejectTest {


	@Test
	fun testTerminate() {
		writerShouldFail { terminate() }
		writerShouldFail { writeListStart(); terminate() }
		writerShouldFail { writeMapStart(); terminate() }
	}


	@Test
	fun testWithErrorChecking() {
		writerShouldFail {
			markAsErrored()
			withErrorChecking { }
		}
	}


	@Test
	fun tesWriteWithNonStringMapKey() {
		writerShouldFail { writeMapStart(); writeInt(0) }
		writerShouldFail { writeMapStart(); writeMapKey(""); writeNull(); writeInt(0) }
	}


	@Test
	fun testWriteAfterEnd() {
		writerShouldFail { writeInt(0); writeInt(0) }
	}


	@Test
	fun testWriteAfterClose() {
		writerShouldFail { writeNull(); close(); writeInt(0) }
	}


	@Test
	fun testWriteListEndOutsideList() {
		writerShouldFail { writeListEnd() }
	}


	@Test
	fun testWriteListEndAfterClose() {
		writerShouldFail { writeNull(); close(); writeListEnd() }
	}


	@Test
	fun testWriteMapEndAfterKey() {
		writerShouldFail { writeMapStart(); writeMapKey(""); writeMapEnd() }
	}


	@Test
	fun testWriteMapEndOutsideMap() {
		writerShouldFail { writeMapEnd() }
	}


	@Test
	fun testWriteMapEndAfterClose() {
		writerShouldFail { writeNull(); close(); writeMapEnd() }
	}


	@Test
	fun testWriteValueWithUnsupportedType() {
		writerShouldFail { writeValue(object {}) }
	}


	private inline fun writerShouldFail(block: JSONWriter.() -> Unit) {
		try {
			StandardWriter(StringWriter()).block()
			throw AssertionError("should fail with a JSONException")
		}
		catch (e: JSONException) {
			// good
		}
		catch (e: IOException) {
			// good
		}
	}
}