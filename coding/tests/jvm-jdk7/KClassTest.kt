package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


class KClassTest {

	@Test
	fun testBoxed() {
		expect(Boolean::class.boxed).toBe(java.lang.Boolean::class)
		expect(Byte::class.boxed).toBe(java.lang.Byte::class)
		expect(Char::class.boxed).toBe(java.lang.Character::class)
		expect(Double::class.boxed).toBe(java.lang.Double::class)
		expect(Float::class.boxed).toBe(java.lang.Float::class)
		expect(Int::class.boxed).toBe(java.lang.Integer::class)
		expect(Long::class.boxed).toBe(java.lang.Long::class)
		expect(Short::class.boxed).toBe(java.lang.Short::class)
		expect(String::class.boxed).toBe(String::class)
		expect(Void.TYPE.kotlin.boxed).toBe(java.lang.Void::class)
	}


	@Test
	fun testIsAssignableOrBoxableFrom() {
		expect(String::class.isAssignableOrBoxableFrom(Any::class))
			.toBe(false)

		expect(Any::class.isAssignableOrBoxableFrom(String::class))
			.toBe(true)

		expect(Boolean::class.isAssignableOrBoxableFrom(java.lang.Boolean::class))
			.toBe(true)

		expect(java.lang.Boolean::class.isAssignableOrBoxableFrom(Boolean::class))
			.toBe(true)
	}


	@Test
	fun testIsAssignableOrBoxableTo() {
		expect(String::class.isAssignableOrBoxableTo(Any::class))
			.toBe(true)

		expect(Any::class.isAssignableOrBoxableTo(String::class))
			.toBe(false)

		expect(Boolean::class.isAssignableOrBoxableTo(java.lang.Boolean::class))
			.toBe(true)

		expect(java.lang.Boolean::class.isAssignableOrBoxableTo(Boolean::class))
			.toBe(true)
	}
}
