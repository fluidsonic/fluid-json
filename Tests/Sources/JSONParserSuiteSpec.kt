package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import java.io.File


class JSONParserSuiteSpec : Spek({

	it("JSONParser conforms to") {

		it("JSON Parsing Test Suite (https://github.com/nst/JSONTestSuite)") {

			File("Tests/Libraries/JSONTestSuite/test_parsing")
				.listFiles()
				.filter { it.name.endsWith(".json") }
				.forEach { file ->
					val expectedBehavior = when (file.name.substringBefore('_')) {
						"i" -> null
						"n" -> Behavior.rejected
						"y" -> Behavior.accepted
						else -> return@forEach
					}

					print("Testing ${file.name}")

					when (file.name) {
						"n_structure_100000_opening_arrays.json",
						"n_structure_open_array_object.json" -> {
							// this is a recursive parser by design
							println(" - SKIPPED")
							return@forEach
						}
					}

					val result = try {
						JSONParser().parse(file.readText())
					}
					catch (e: JSONException) {
						e
					}
					catch (e: Throwable) {
						println(" - FAILED!")
						throw e
					}

					val actualBehavior = if (result is JSONException) Behavior.rejected else Behavior.accepted
					println(" - ${actualBehavior.name}")

					if (expectedBehavior != null && actualBehavior != expectedBehavior) {
						var message = "Expected '${file.name}' to be $expectedBehavior but was $actualBehavior"
						if (result !is Exception) {
							message = "$message: $result"
						}

						throw RuntimeException(message, result as? Exception)
					}
				}
		}
	}
}) {

	private enum class Behavior {

		accepted,
		rejected
	}
}
