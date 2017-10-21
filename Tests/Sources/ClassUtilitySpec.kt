package tests

import com.github.fluidsonic.fluid.json.boxed
import com.github.fluidsonic.fluid.json.isAssignableOrBoxableFrom
import com.github.fluidsonic.fluid.json.isAssignableOrBoxableTo
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object ClassUtilitySpec : Spek({

	describe("Class Utility") {

		it(".boxed") {
			Boolean::class.java.boxed.should.equal(java.lang.Boolean::class.java)
			Byte::class.java.boxed.should.equal(java.lang.Byte::class.java)
			Char::class.java.boxed.should.equal(java.lang.Character::class.java)
			Double::class.java.boxed.should.equal(java.lang.Double::class.java)
			Float::class.java.boxed.should.equal(java.lang.Float::class.java)
			Int::class.java.boxed.should.equal(java.lang.Integer::class.java)
			Long::class.java.boxed.should.equal(java.lang.Long::class.java)
			Short::class.java.boxed.should.equal(java.lang.Short::class.java)
			String::class.java.boxed.should.equal(String::class.java)
			Void.TYPE.boxed.should.equal(java.lang.Void::class.java)
		}

		it("isAssignableOrBoxableFrom()") {
			String::class.java.isAssignableOrBoxableFrom(Any::class.java)
				.should.be.`false`

			Any::class.java.isAssignableOrBoxableFrom(String::class.java)
				.should.be.`true`

			Boolean::class.java.isAssignableOrBoxableFrom(java.lang.Boolean::class.java)
				.should.be.`true`

			java.lang.Boolean::class.java.isAssignableOrBoxableFrom(Boolean::class.java)
				.should.be.`true`
		}

		it("isAssignableOrBoxableTo()") {
			String::class.java.isAssignableOrBoxableTo(Any::class.java)
				.should.be.`true`

			Any::class.java.isAssignableOrBoxableTo(String::class.java)
				.should.be.`false`

			Boolean::class.java.isAssignableOrBoxableTo(java.lang.Boolean::class.java)
				.should.be.`true`

			java.lang.Boolean::class.java.isAssignableOrBoxableTo(Boolean::class.java)
				.should.be.`true`
		}
	}
})
