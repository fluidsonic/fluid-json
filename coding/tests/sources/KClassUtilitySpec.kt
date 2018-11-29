package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


internal object KClassUtilitySpec : Spek({

	describe("Class Utility") {

		it(".boxed") {
			Boolean::class.boxed.should.equal(java.lang.Boolean::class)
			Byte::class.boxed.should.equal(java.lang.Byte::class)
			Char::class.boxed.should.equal(java.lang.Character::class)
			Double::class.boxed.should.equal(java.lang.Double::class)
			Float::class.boxed.should.equal(java.lang.Float::class)
			Int::class.boxed.should.equal(java.lang.Integer::class)
			Long::class.boxed.should.equal(java.lang.Long::class)
			Short::class.boxed.should.equal(java.lang.Short::class)
			String::class.boxed.should.equal(String::class)
			Void.TYPE.kotlin.boxed.should.equal(java.lang.Void::class)
		}

		it("isAssignableOrBoxableFrom()") {
			String::class.isAssignableOrBoxableFrom(Any::class)
				.should.be.`false`

			Any::class.isAssignableOrBoxableFrom(String::class)
				.should.be.`true`

			Boolean::class.isAssignableOrBoxableFrom(java.lang.Boolean::class)
				.should.be.`true`

			java.lang.Boolean::class.isAssignableOrBoxableFrom(Boolean::class)
				.should.be.`true`
		}

		it("isAssignableOrBoxableTo()") {
			String::class.isAssignableOrBoxableTo(Any::class)
				.should.be.`true`

			Any::class.isAssignableOrBoxableTo(String::class)
				.should.be.`false`

			Boolean::class.isAssignableOrBoxableTo(java.lang.Boolean::class)
				.should.be.`true`

			java.lang.Boolean::class.isAssignableOrBoxableTo(Boolean::class)
				.should.be.`true`
		}
	}
})
