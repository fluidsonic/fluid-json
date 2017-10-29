package tests

import com.github.fluidsonic.fluid.json.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.IOException
import java.io.StringWriter


internal object StandardWriterRejectSpec : Spek({

	describe("StandardWriter fails in") {

		it(".close()") {
			writerShouldFail { close() }
			writerShouldFail { writeListStart(); close() }
			writerShouldFail { writeMapStart(); close() }
		}

		it(".withErrorChecking()") {
			writerShouldFail {
				markAsErrored()
				withErrorChecking { }
			}
		}

		it(".write*() non-string as map key") {
			writerShouldFail { writeMapStart(); writeInt(0) }
			writerShouldFail { writeMapStart(); writeMapKey(""); writeNull(); writeInt(0) }
		}

		it(".write*() at end") {
			writerShouldFail { writeInt(0); writeInt(0) }
		}

		it(".write*() when closed") {
			writerShouldFail { writeNull(); close(); writeInt(0) }
		}

		it(".writeListEnd() when not in list") {
			writerShouldFail { writeListEnd() }
		}

		it(".writeListEnd() when closed") {
			writerShouldFail { writeNull(); close(); writeListEnd() }
		}

		it(".writeMapEnd() when after key") {
			writerShouldFail { writeMapStart(); writeMapKey(""); writeMapEnd() }
		}

		it(".writeMapEnd() when not in list") {
			writerShouldFail { writeMapEnd() }
		}

		it(".writeMapEnd() when closed") {
			writerShouldFail { writeNull(); close(); writeMapEnd() }
		}

		it(".writeValue() for unsupported types") {
			writerShouldFail { writeValue(object {}) }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private inline fun TestBody.writerShouldFail(body: JSONWriter.() -> Unit) {
	try {
		StandardWriter(StringWriter()).body()
		throw AssertionError("should fail with a JSONException")
	}
	catch (e: JSONException) {
		// good
	}
	catch (e: IOException) {
		// good
	}
}
