package tests

import com.github.fluidsonic.fluid.json.JSONWriter
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import java.io.StringWriter


internal object StandardWriterAcceptSpec : SubjectSpek<JSONWriter>({

	subject { JSONWriter.with(StringWriter()) }


	describe("StandardWriter succeeds for") {

		it(".close()") {
			write { writeNull(); close() }
			write { writeNull(); close(); close() }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private fun TestBody.write(body: JSONWriter.() -> Unit): String =
	StringWriter().also { JSONWriter.with(it).body() }.toString()
