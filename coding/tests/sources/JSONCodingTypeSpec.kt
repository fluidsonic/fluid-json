package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object JSONCodingTypeSpec : Spek({

	describe("JSONCodingType") {

		it("supports generic type parameters with multiple upper bounds") {
			jsonCodingType<MultipleUpperBounds<*>>()
		}

		it("supports KClass-based creation") {
			jsonCodingType(List::class).should.equal(jsonCodingType())
			jsonCodingType(Array<Any>::class).should.equal(jsonCodingType())
			jsonCodingType(Map::class).should.equal(jsonCodingType())
			jsonCodingType(String::class).should.equal(jsonCodingType())
		}
	}
}) {

	private interface Interface1
	private interface Interface2
	private interface MultipleUpperBounds<T> where T : Interface1, T : Interface2
}
