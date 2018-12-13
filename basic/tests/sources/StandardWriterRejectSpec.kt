package tests.basic

import com.github.fluidsonic.fluid.json.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.specification.describe
import java.io.IOException
import java.io.StringWriter


internal object StandardWriterRejectSpec : Spek({

	describe("StandardWriter fails in") {

		it("terminate()") {
			writerShouldFail { terminate() }
			writerShouldFail { writeListStart(); terminate() }
			writerShouldFail { writeMapStart(); terminate() }
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
private inline fun TestBody.writerShouldFail(block: JSONWriter.() -> Unit) {
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