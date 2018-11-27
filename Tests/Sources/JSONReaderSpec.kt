package tests

import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object JSONReaderSpec : Spek({

	describe("JSONReader") {

		it(".read*() default implementations") {
			val reader = object : DummyJSONReader() {
				override fun readDouble() = 10.0
			}

			reader.readFloat().should.equal(10.0f)
		}
	}
})
