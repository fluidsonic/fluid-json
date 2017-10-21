package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parse
import com.github.fluidsonic.fluid.json.parseOfType
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.Reader
import java.io.StringReader


internal object JSONParserSpec : Spek({

	describe("JSONParser") {

		it(".default() returns a default parser") {
			val defaultParser = JSONParser.default()
			plainData.testDecoding(defaultParser::parse)
		}

		it(".parse() shortcuts pass correct values") {
			var expectedContext: TestCoderContext? = null
			var expectedValueClass: Class<*>? = null

			val parser = object : JSONParser<JSONCoderContext> {
				override fun <Value : Any> parse(source: Reader, valueClass: Class<out Value>, context: JSONCoderContext): Value? {
					source.should.be.instanceof(StringReader::class.java)
					source as StringReader
					source.readText().should.equal("okay")

					if (expectedValueClass != null) {
						(valueClass as Class<*>).should.equal(expectedValueClass)
					}
					if (expectedContext != null) {
						context.should.equal(expectedContext)
					}

					return null
				}
			}

			parser.parse("okay")
			parser.parse(StringReader("okay"))

			expectedContext = TestCoderContext()
			parser.parse("okay", expectedContext)
			parser.parse(StringReader("okay"), expectedContext)

			expectedValueClass = String::class.java
			expectedContext = null
			parser.parse("okay", valueClass = expectedValueClass)
			parser.parse(StringReader("okay"), valueClass = String::class.java)
			parser.parseOfType<String>("okay")
			parser.parseOfType<String>(StringReader("okay"))

			expectedContext = TestCoderContext()
			parser.parse("okay", valueClass = expectedValueClass, context = expectedContext)
			parser.parseOfType<String, TestCoderContext>("okay", context = expectedContext)
			parser.parseOfType<String, TestCoderContext>(StringReader("okay"), context = expectedContext)
		}
	}
})
