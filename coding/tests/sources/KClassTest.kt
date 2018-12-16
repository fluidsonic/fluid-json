package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


internal object KClassTest {

	@Test
	fun testBoxed() {
		assert(Boolean::class.boxed).toBe(java.lang.Boolean::class)
		assert(Byte::class.boxed).toBe(java.lang.Byte::class)
		assert(Char::class.boxed).toBe(java.lang.Character::class)
		assert(Double::class.boxed).toBe(java.lang.Double::class)
		assert(Float::class.boxed).toBe(java.lang.Float::class)
		assert(Int::class.boxed).toBe(java.lang.Integer::class)
		assert(Long::class.boxed).toBe(java.lang.Long::class)
		assert(Short::class.boxed).toBe(java.lang.Short::class)
		assert(String::class.boxed).toBe(String::class)
		assert(Void.TYPE.kotlin.boxed).toBe(java.lang.Void::class)
	}


	@Test
	fun testIsAssignableOrBoxableFrom() {
		assert(String::class.isAssignableOrBoxableFrom(Any::class))
			.toBe(false)

		assert(Any::class.isAssignableOrBoxableFrom(String::class))
			.toBe(true)

		assert(Boolean::class.isAssignableOrBoxableFrom(java.lang.Boolean::class))
			.toBe(true)

		assert(java.lang.Boolean::class.isAssignableOrBoxableFrom(Boolean::class))
			.toBe(true)
	}


	@Test
	fun testIsAssignableOrBoxableTo() {
		assert(String::class.isAssignableOrBoxableTo(Any::class))
			.toBe(true)

		assert(Any::class.isAssignableOrBoxableTo(String::class))
			.toBe(false)

		assert(Boolean::class.isAssignableOrBoxableTo(java.lang.Boolean::class))
			.toBe(true)

		assert(java.lang.Boolean::class.isAssignableOrBoxableTo(Boolean::class))
			.toBe(true)
	}
}
