package tests

import com.github.fluidsonic.fluid.json.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object JSONCodableTypeSpec : Spek({

	describe("JSONCodableType") {

		it("supports generic type parameters with multiple upper bounds") {
			jsonCodableType<MultipleUpperBounds<*>>()
		}
	}
}) {

	private interface Interface1
	private interface Interface2
	private interface MultipleUpperBounds<T> where T : Interface1, T : Interface2
}
