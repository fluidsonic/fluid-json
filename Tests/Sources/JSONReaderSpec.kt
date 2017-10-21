package tests

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object JSONReaderSpec : Spek({

	describe("JSONReader") {

		it(".readFloat() delegates to .readDouble() by default") {
			val reader = object : DummyJSONReader() {
				override fun readDouble() = 10.0
			}

			reader.readFloat().should.equal(10.0f)
		}
	}
})
