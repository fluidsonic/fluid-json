package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


internal object JSONCodingTypeTest {

	@Test
	fun testGenericTypeParametersWithMultipleUpperBounds() {
		jsonCodingType<MultipleUpperBounds<*>>()
	}


	@Test
	fun testKClassBasedCreation() {
		assert(jsonCodingType(List::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Array<Any>::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Map::class)).toBe(jsonCodingType())
		assert(jsonCodingType(String::class)).toBe(jsonCodingType())
	}


	@Test
	fun testRecursiveGenerics() {
		assert(jsonCodingType(ClosedRange::class)).toBe(jsonCodingType())
	}


	private interface Interface1
	private interface Interface2
	private interface MultipleUpperBounds<T> where T : Interface1, T : Interface2
}
