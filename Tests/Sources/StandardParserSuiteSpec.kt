package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.StandardParser
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.File
import java.io.FileReader


internal object StandardParserSuiteSpec : Spek({

	describe("StandardParser conforms to") {

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

					val result = try {
						StandardParser().parse(FileReader(file))
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
