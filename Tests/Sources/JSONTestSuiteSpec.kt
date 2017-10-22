package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.StandardParser
import com.github.fluidsonic.fluid.json.parse
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.File
import java.io.FileReader


internal object JSONTestSuiteSpec : Spek({

	describe("JSON Test Suite (https://github.com/nst/JSONTestSuite)") {
		val parser = StandardParser(JSONCoderContext.empty) { source, context ->
			JSONDecoder.with(source, context = context, codecResolver = JSONCodecResolver.plain)
		}

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

				it(file.name) {
					val result = try {
						parser.parse(FileReader(file))
					}
					catch (e: JSONException) {
						e
					}

					val actualBehavior = if (result is JSONException) Behavior.rejected else Behavior.accepted

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
