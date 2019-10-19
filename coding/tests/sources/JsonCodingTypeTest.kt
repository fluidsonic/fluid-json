package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object JsonCodingTypeTest {

	@Test
	fun testGenericTypeParametersWithMultipleUpperBounds() {
		jsonCodingType<MultipleUpperBounds<*>>()
	}


	@Test
	fun testKClassBasedCreation() {
		assert(jsonCodingType(List::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Array<Any>::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Array<String>::class, String::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Map::class)).toBe(jsonCodingType())
		assert(jsonCodingType(Map::class, String::class, Int::class)).toBe(jsonCodingType<Map<String, Int>>())
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
