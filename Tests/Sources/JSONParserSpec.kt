package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONNullability
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseList
import com.github.fluidsonic.fluid.json.parseListOfType
import com.github.fluidsonic.fluid.json.parseMap
import com.github.fluidsonic.fluid.json.parseMapOfType
import com.github.fluidsonic.fluid.json.parseValue
import com.github.fluidsonic.fluid.json.parseValueOfType
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.Reader
import java.io.StringReader
import java.time.LocalDate
import kotlin.reflect.KClass


internal object JSONParserSpec : Spek({

	describe("JSONParser") {

		it(".builder()") {
			JSONParser.builder()
				.decodingWith(JSONCodecResolver.default)
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					parseValue(StringReader("true")).should.equal(true)
				}

			JSONParser.builder()
				.decodingWith(BooleanJSONCodec)
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					parseValue(StringReader("true")).should.equal(true)
				}

			JSONParser.builder()
				.decodingWith(listOf(BooleanJSONCodec))
				.build()
				.apply {
					context.should.equal(JSONCoderContext.empty)
					parseValue(StringReader("true")).should.equal(true)
				}

			val testContext = TestCoderContext()

			JSONParser.builder(testContext)
				.decodingWith(JSONCodecResolver.default)
				.build()
				.apply {
					context.should.equal(testContext)
					parseValue(StringReader("true")).should.equal(true)
				}
		}

		it(".default()") {
			anyData.testDecoding(JSONParser.default()::parseValue)
		}

		it(".withContext()") {
			val testContext = TestCoderContext()

			JSONParser.default()
				.withContext(testContext)
				.context.should.equal(testContext)
		}

		it(".parse()") {
			val parentParser = JSONParser.builder()
				.decodingWith(LocalDateCodec)
				.build()

			val parser = object : JSONParser<JSONCoderContext> by parentParser {

				override fun <Key : Any, Value : Any> doParseMapWithClasses(
					source: Reader,
					keyClass: KClass<out Key>,
					valueClass: KClass<out Value>,
					nullability: JSONNullability.Key
				) =
					parentParser.doParseMapWithClasses(source, keyClass, valueClass, nullability)
						?.mapKeys { null as Key? }


				override fun <Key : Any, Value : Any> doParseMapWithClasses(
					source: Reader,
					keyClass: KClass<out Key>,
					valueClass: KClass<out Value>,
					nullability: JSONNullability.KeyAndValue
				) =
					parentParser.doParseMapWithClasses(source, keyClass, valueClass, nullability)
						?.mapKeys { null as Key? }
			}

			parser.parseValue("true")
				.should.equal(true)

			parser.parseValue(StringReader("true"))
				.should.equal(true)

			parser.parseList("[true]")
				.should.equal(listOf(true))

			parser.parseList(StringReader("[true]"))
				.should.equal(listOf(true))

			parser.parseList("[true,null]", nullability = JSONNullability.Value)
				.should.equal(listOf(true, null))

			parser.parseList(StringReader("[true,null]"), nullability = JSONNullability.Value)
				.should.equal(listOf(true, null))

			parser.parseMap("""{"key":true}""")
				.should.equal(mapOf("key" to true))

			parser.parseMap(StringReader("""{"key":true}"""))
				.should.equal(mapOf("key" to true))

			parser.parseMap("""{"key":true}""", nullability = JSONNullability.Key)
				.should.equal(mapOf(null to true))

			parser.parseMap(StringReader("""{"key":true}"""), nullability = JSONNullability.Key)
				.should.equal(mapOf(null to true))

			parser.parseMap("""{"key":null}""", nullability = JSONNullability.KeyAndValue)
				.should.equal(mapOf(null to null))

			parser.parseMap(StringReader("""{"key":null}"""), nullability = JSONNullability.KeyAndValue)
				.should.equal(mapOf(null to null))

			parser.parseMap("""{"key":null}""", nullability = JSONNullability.Value)
				.should.equal(mapOf("key" to null))

			parser.parseMap(StringReader("""{"key":null}"""), nullability = JSONNullability.Value)
				.should.equal(mapOf("key" to null))

			val testValue = LocalDate.of(2018, 1, 1)

			parser.parseValueOfType<LocalDate>("\"2018-01-01\"")
				.should.equal(testValue)

			parser.parseValueOfType<LocalDate>(StringReader("\"2018-01-01\""))
				.should.equal(testValue)

			parser.parseListOfType<LocalDate>("""["2018-01-01"]""")
				.should.equal(listOf(testValue))

			parser.parseListOfType<LocalDate>(StringReader("""["2018-01-01"]"""))
				.should.equal(listOf(testValue))

			parser.parseListOfType<LocalDate>("""["2018-01-01",null]""", nullability = JSONNullability.Value)
				.should.equal(listOf(testValue, null))

			parser.parseListOfType<LocalDate>(StringReader("""["2018-01-01",null]"""), nullability = JSONNullability.Value)
				.should.equal(listOf(testValue, null))

			parser.parseMapOfType<LocalDate, LocalDate>("""{"2018-01-01":"2018-01-01"}""")
				.should.equal(mapOf(testValue to testValue))

			parser.parseMapOfType<LocalDate, LocalDate>(StringReader("""{"2018-01-01":"2018-01-01"}"""))
				.should.equal(mapOf(testValue to testValue))

			parser.parseMapOfType<LocalDate, LocalDate>("""{"2018-01-01":"2018-01-01"}""", nullability = JSONNullability.Key)
				.should.equal(mapOf(null to testValue))

			parser.parseMapOfType<LocalDate, LocalDate>(StringReader("""{"2018-01-01":"2018-01-01"}"""), nullability = JSONNullability.Key)
				.should.equal(mapOf(null to testValue))

			parser.parseMapOfType<LocalDate, LocalDate>("""{"2018-01-01":null}""", nullability = JSONNullability.KeyAndValue)
				.should.equal(mapOf(null to null))

			parser.parseMapOfType<LocalDate, LocalDate>(StringReader("""{"2018-01-01":null}"""), nullability = JSONNullability.KeyAndValue)
				.should.equal(mapOf(null to null))

			parser.parseMapOfType<LocalDate, LocalDate>("""{"2018-01-01":null}""", nullability = JSONNullability.Value)
				.should.equal(mapOf(testValue to null))

			parser.parseMapOfType<LocalDate, LocalDate>(StringReader("""{"2018-01-01":null}"""), nullability = JSONNullability.Value)
				.should.equal(mapOf(testValue to null))
		}
	}
})
