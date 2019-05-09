package tests.basic

import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


internal object CompanionTest {

	@Test
	fun testInitialization() {
		// just make sure all companions initialize properly and we get more code coverage :)
		listOf(
			JSONException.Companion,
			JSONReader.Companion,
			JSONToken.Companion,
			JSONWriter.Companion
		)

		// trigger initialization of Character.* objects to make code coverage happy
		JSONCharacter::class.nestedClasses.forEach {
			try {
				it.objectInstance
			}
			catch (e: IllegalAccessException) {
				// ignore
			}
		}
	}
}
