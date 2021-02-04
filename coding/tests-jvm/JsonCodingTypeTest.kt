package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


class JsonCodingTypeTest {

	@Test
	fun testGenericTypeParametersWithMultipleUpperBounds() {
		jsonCodingType<MultipleUpperBounds<*>>()
	}


	@Test
	@Suppress("UNCHECKED_CAST")
	fun testKClassBasedCreation() {
		expect(jsonCodingType(List::class)).toBe(jsonCodingType())
		expect(jsonCodingType(Array<Any>::class)).toBe(jsonCodingType())
		expect(jsonCodingType(Array<String>::class, String::class)).toBe(jsonCodingType())
		expect(jsonCodingType(Map::class)).toBe(jsonCodingType())
		expect(jsonCodingType(Map::class, String::class, Int::class)).toBe(jsonCodingType<Map<String, Int>>() as JsonCodingType<Map<*, *>>)
		expect(jsonCodingType(String::class)).toBe(jsonCodingType())
	}


	@Test
	fun testRecursiveGenerics() {
		expect(jsonCodingType(ClosedRange::class)).toBe(jsonCodingType())
	}


	private interface Interface1
	private interface Interface2
	private interface MultipleUpperBounds<T> where T : Interface1, T : Interface2
}
