package tests.basic

import io.fluidsonic.json.*
import kotlin.test.*


class CompanionTest {

	@Test
	fun testInitialization() {
		// just make sure all companions initialize properly and we get more code coverage :)
		listOf(
			JsonException.Companion,
			JsonReader.Companion,
			JsonToken.Companion,
			JsonWriter.Companion
		)

		// trigger initialization of Character.* objects to make code coverage happy
		JsonCharacter::class.nestedClasses.forEach {
			try {
				it.objectInstance
			}
			catch (e: IllegalAccessException) {
				// ignore
			}
		}
	}
}
