package tests.coding

import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*


@DisplayName("JSON Test Suite (https://github.com/nst/JSONTestSuite)")
internal object JsonTestSuite {

	private val environments = listOf(
		TestEnvironment("Default Parser", JsonCodingParser.default, parserIsRecursive = true),
		TestEnvironment("Non-recursive Parser", JsonCodingParser.nonRecursive, parserIsRecursive = false)
	)


	private val nonRecursiveOnlyFileNames = setOf(
		"n_structure_100000_opening_arrays.json",
		"n_structure_open_array_object.json"
	)


	private fun buildTest(environment: TestEnvironment, file: File) =
		DynamicTest.dynamicTest(file.name) {
			val result = try {
				environment.parser.parseValueOrNull(FileReader(file))
			}
			catch (e: JsonException) {
				e
			}
			catch (e: StackOverflowError) {
				throw AssertionError("Stack overflow in '${file.name}'")
			}

			val actualBehavior = if (result is JsonException) Behavior.rejected else Behavior.accepted
			val expectedBehavior = when (file.name.substringBefore('_')) {
				"i" -> null
				"n" -> Behavior.rejected
				"y" -> Behavior.accepted
				else -> error("Cannot process file '$file'")
			}

			if (expectedBehavior != null && actualBehavior != expectedBehavior) {
				var message = "Expected '${file.name}' to be $expectedBehavior but was $actualBehavior"
				if (result !is Exception) {
					message = "$message: $result"
				}

				throw RuntimeException(message, result as? Exception)
			}
		}!!


	@TestFactory
	fun test() =
		environments.map { environment ->
			DynamicContainer.dynamicContainer(
				environment.name,
				File("../dependencies/JSONTestSuite/test_parsing")
					.listFiles()
					.sortedBy { it.name }
					.filter { it.name.endsWith(".json") }
					.filter { !environment.parserIsRecursive || !nonRecursiveOnlyFileNames.contains(it.name) }
					.mapNotNull { buildTest(environment = environment, file = it) }
			)!!
		}


	private enum class Behavior {

		accepted,
		rejected
	}


	private class TestEnvironment(
		val name: String,
		val parser: JsonCodingParser<*>,
		val parserIsRecursive: Boolean
	)
}