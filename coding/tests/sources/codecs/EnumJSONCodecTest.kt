package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.json.EnumJSONTransformation.*
import org.junit.jupiter.api.Test


@Suppress("UNCHECKED_CAST")
internal object EnumJSONCodecTest {

	private val samplesByPropertyAndCase = mapOf(
		"name" to mapOf(
			null to listOf(
				"lowerCamelCase123",
				"lower_snake_case123",
				"lowercase123",
				"lowercase words 123",
				"UpperCamelCase123",
				"UPPER_SNAKE_CASE123",
				"UPPERCASE123",
				"UPPERCASE WORDS 123",
				"0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec@1_0"
			),
			Case.lowerCamelCase to listOf(
				"lowerCamelCase123",
				"lowerSnakeCase123",
				"lowercase123",
				"lowercaseWords123",
				"upperCamelCase123",
				"upperSnakeCase123",
				"uppercase123",
				"uppercaseWords123",
				"0SomeSpacesUpperCaseWtfSnakesAreComingIn123Abc123JustHtmlXmlXmlJsonCodec1_0"
			),
			Case.lower_snake_case to listOf(
				"lower_camel_case_123",
				"lower_snake_case_123",
				"lowercase_123",
				"lowercase_words_123",
				"upper_camel_case_123",
				"upper_snake_case_123",
				"uppercase_123",
				"uppercase_words_123",
				"0_some_spaces_upper_case_wtf_snakes_are_coming_in_123_abc_123_just_html_xml_xml_json_codec_1_0"
			),
			Case.lowercase to listOf(
				"lowercamelcase123",
				"lower_snake_case123",
				"lowercase123",
				"lowercase words 123",
				"uppercamelcase123",
				"upper_snake_case123",
				"uppercase123",
				"uppercase words 123",
				"0 some spaces uppercase wtf snakes_are_coming_in 123 abc123 justhtml xml xml jsoncodec@1_0"
			),
			Case.`lowercase words` to listOf(
				"lower camel case 123",
				"lower snake case 123",
				"lowercase 123",
				"lowercase words 123",
				"upper camel case 123",
				"upper snake case 123",
				"uppercase 123",
				"uppercase words 123",
				"0 some spaces upper case wtf snakes are coming in 123 abc 123 just html xml xml json codec 1_0"
			),
			Case.UpperCamelCase to listOf(
				"LowerCamelCase123",
				"LowerSnakeCase123",
				"Lowercase123",
				"LowercaseWords123",
				"UpperCamelCase123",
				"UpperSnakeCase123",
				"Uppercase123",
				"UppercaseWords123",
				"0SomeSpacesUpperCaseWtfSnakesAreComingIn123Abc123JustHtmlXmlXmlJsonCodec1_0"
			),
			Case.UPPER_SNAKE_CASE to listOf(
				"LOWER_CAMEL_CASE_123",
				"LOWER_SNAKE_CASE_123",
				"LOWERCASE_123",
				"LOWERCASE_WORDS_123",
				"UPPER_CAMEL_CASE_123",
				"UPPER_SNAKE_CASE_123",
				"UPPERCASE_123",
				"UPPERCASE_WORDS_123",
				"0_SOME_SPACES_UPPER_CASE_WTF_SNAKES_ARE_COMING_IN_123_ABC_123_JUST_HTML_XML_XML_JSON_CODEC_1_0"
			),
			Case.UPPERCASE to listOf(
				"LOWERCAMELCASE123",
				"LOWER_SNAKE_CASE123",
				"LOWERCASE123",
				"LOWERCASE WORDS 123",
				"UPPERCAMELCASE123",
				"UPPER_SNAKE_CASE123",
				"UPPERCASE123",
				"UPPERCASE WORDS 123",
				"0 SOME SPACES UPPERCASE WTF SNAKES_ARE_COMING_IN 123 ABC123 JUSTHTML XML XML JSONCODEC@1_0"
			),
			Case.`UPPERCASE WORDS` to listOf(
				"LOWER CAMEL CASE 123",
				"LOWER SNAKE CASE 123",
				"LOWERCASE 123",
				"LOWERCASE WORDS 123",
				"UPPER CAMEL CASE 123",
				"UPPER SNAKE CASE 123",
				"UPPERCASE 123",
				"UPPERCASE WORDS 123",
				"0 SOME SPACES UPPER CASE WTF SNAKES ARE COMING IN 123 ABC 123 JUST HTML XML XML JSON CODEC 1_0"
			)
		),
		"toString" to mapOf(
			null to listOf(
				"lowerCamelCase123.toString()",
				"lower_snake_case123.toString()",
				"lowercase123.toString()",
				"lowercase words 123.toString()",
				"UpperCamelCase123.toString()",
				"UPPER_SNAKE_CASE123.toString()",
				"UPPERCASE123.toString()",
				"UPPERCASE WORDS 123.toString()",
				"0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec/1.0.toString()"
			),
			Case.lowerCamelCase to listOf(
				"lowerCamelCase123ToString",
				"lowerSnakeCase123ToString",
				"lowercase123ToString",
				"lowercaseWords123ToString",
				"upperCamelCase123ToString",
				"upperSnakeCase123ToString",
				"uppercase123ToString",
				"uppercaseWords123ToString",
				"0SomeSpacesUpperCaseWtfSnakesAreComingIn123Abc123JustHtmlXmlXmlJsonCodec1.0ToString"
			),
			Case.lower_snake_case to listOf(
				"lower_camel_case_123_to_string",
				"lower_snake_case_123_to_string",
				"lowercase_123_to_string",
				"lowercase_words_123_to_string",
				"upper_camel_case_123_to_string",
				"upper_snake_case_123_to_string",
				"uppercase_123_to_string",
				"uppercase_words_123_to_string",
				"0_some_spaces_upper_case_wtf_snakes_are_coming_in_123_abc_123_just_html_xml_xml_json_codec_1.0_to_string"
			),
			Case.lowercase to listOf(
				"lowercamelcase123.tostring()",
				"lower_snake_case123.tostring()",
				"lowercase123.tostring()",
				"lowercase words 123.tostring()",
				"uppercamelcase123.tostring()",
				"upper_snake_case123.tostring()",
				"uppercase123.tostring()",
				"uppercase words 123.tostring()",
				"0 some spaces uppercase wtf snakes_are_coming_in 123 abc123 justhtml xml xml jsoncodec/1.0.tostring()"
			),
			Case.`lowercase words` to listOf(
				"lower camel case 123 to string",
				"lower snake case 123 to string",
				"lowercase 123 to string",
				"lowercase words 123 to string",
				"upper camel case 123 to string",
				"upper snake case 123 to string",
				"uppercase 123 to string",
				"uppercase words 123 to string",
				"0 some spaces upper case wtf snakes are coming in 123 abc 123 just html xml xml json codec 1.0 to string"
			),
			Case.UpperCamelCase to listOf(
				"LowerCamelCase123ToString",
				"LowerSnakeCase123ToString",
				"Lowercase123ToString",
				"LowercaseWords123ToString",
				"UpperCamelCase123ToString",
				"UpperSnakeCase123ToString",
				"Uppercase123ToString",
				"UppercaseWords123ToString",
				"0SomeSpacesUpperCaseWtfSnakesAreComingIn123Abc123JustHtmlXmlXmlJsonCodec1.0ToString"
			),
			Case.UPPER_SNAKE_CASE to listOf(
				"LOWER_CAMEL_CASE_123_TO_STRING",
				"LOWER_SNAKE_CASE_123_TO_STRING",
				"LOWERCASE_123_TO_STRING",
				"LOWERCASE_WORDS_123_TO_STRING",
				"UPPER_CAMEL_CASE_123_TO_STRING",
				"UPPER_SNAKE_CASE_123_TO_STRING",
				"UPPERCASE_123_TO_STRING",
				"UPPERCASE_WORDS_123_TO_STRING",
				"0_SOME_SPACES_UPPER_CASE_WTF_SNAKES_ARE_COMING_IN_123_ABC_123_JUST_HTML_XML_XML_JSON_CODEC_1.0_TO_STRING"
			),
			Case.UPPERCASE to listOf(
				"LOWERCAMELCASE123.TOSTRING()",
				"LOWER_SNAKE_CASE123.TOSTRING()",
				"LOWERCASE123.TOSTRING()",
				"LOWERCASE WORDS 123.TOSTRING()",
				"UPPERCAMELCASE123.TOSTRING()",
				"UPPER_SNAKE_CASE123.TOSTRING()",
				"UPPERCASE123.TOSTRING()",
				"UPPERCASE WORDS 123.TOSTRING()",
				"0 SOME SPACES UPPERCASE WTF SNAKES_ARE_COMING_IN 123 ABC123 JUSTHTML XML XML JSONCODEC/1.0.TOSTRING()"
			),
			Case.`UPPERCASE WORDS` to listOf(
				"LOWER CAMEL CASE 123 TO STRING",
				"LOWER SNAKE CASE 123 TO STRING",
				"LOWERCASE 123 TO STRING",
				"LOWERCASE WORDS 123 TO STRING",
				"UPPER CAMEL CASE 123 TO STRING",
				"UPPER SNAKE CASE 123 TO STRING",
				"UPPERCASE 123 TO STRING",
				"UPPERCASE WORDS 123 TO STRING",
				"0 SOME SPACES UPPER CASE WTF SNAKES ARE COMING IN 123 ABC 123 JUST HTML XML XML JSON CODEC 1.0 TO STRING"
			)
		)
	)


	private fun enumCodec(property: String, case: Case? = null) =
		EnumJSONCodec<Example>(transformation = when (property) {
			"name" -> EnumJSONTransformation.Name(case = case)
			"toString" -> EnumJSONTransformation.ToString(case = case)
			else -> error("unknown property")
		})


	@Test
	fun testOrdinal() {
		val codec = EnumJSONCodec<Example>(transformation = EnumJSONTransformation.Ordinal)

		val parser = JSONCodingParser
			.builder()
			.decodingWith(codec)
			.build()

		val serializer = JSONCodingSerializer
			.builder()
			.encodingWith(codec)
			.build()

		fun fail(
			message: String,
			input: String,
			expected: String,
			actual: String
		): Nothing {
			throw AssertionError(
				"$message:\n" +
					"property = ordinal\n" +
					"input = $input\n" +
					"expected = $expected\n" +
					"actual = $actual"
			)
		}

		for (expectedValue in Example.values()) {
			val expectedJSON = serializer.serializeValue(expectedValue.ordinal)

			val actualJSON = serializer.serializeValue(expectedValue)
			if (actualJSON != expectedJSON)
				fail(
					message = "enum was not serialized correctly",
					input = "Example.${expectedValue.kotlinIdentifier}",
					expected = expectedJSON,
					actual = actualJSON
				)

			val actualValue = parser.parseValueOfType<Example>(expectedJSON)
			if (actualValue != expectedValue)
				fail(
					message = "enum was not parser correctly",
					input = expectedJSON,
					expected = "Example.${expectedValue.kotlinIdentifier}",
					actual = "Example.${actualValue.kotlinIdentifier}"
				)
		}
	}


	@Test
	fun testProperties() {
		for ((property, samplesByCase) in samplesByPropertyAndCase) {
			for ((case, samples) in samplesByCase) {
				fun fail(
					message: String,
					input: String,
					expected: String,
					actual: String
				): Nothing {
					throw AssertionError(
						"$message:\n" +
							"property = $property\n" +
							"case = $case\n" +
							"input = $input\n" +
							"expected = $expected\n" +
							"actual = $actual"
					)
				}

				val parser = JSONCodingParser
					.builder()
					.decodingWith(enumCodec(property = property, case = case))
					.build()

				val serializer = JSONCodingSerializer
					.builder()
					.encodingWith(enumCodec(property = property, case = case))
					.build()

				for ((ordinal, sample) in samples.withIndex()) {
					val expectedValue = Example.values()[ordinal]
					val expectedJSON = serializer.serializeValue(sample)

					val actualJSON = serializer.serializeValue(expectedValue)
					if (actualJSON != expectedJSON)
						fail(
							message = "enum was not serialized correctly",
							input = "Example.${expectedValue.kotlinIdentifier}",
							expected = expectedJSON,
							actual = actualJSON
						)

					val actualValue = parser.parseValueOfType<Example>(expectedJSON)
					if (actualValue != expectedValue)
						fail(
							message = "enum was not parsed correctly",
							input = expectedJSON,
							expected = "Example.${expectedValue.kotlinIdentifier}",
							actual = "Example.${actualValue.kotlinIdentifier}"
						)
				}
			}
		}
	}


	@Suppress("EnumEntryName")
	private enum class Example {
		lowerCamelCase123,
		lower_snake_case123,
		lowercase123,
		`lowercase words 123`,
		UpperCamelCase123,
		UPPER_SNAKE_CASE123,
		UPPERCASE123,
		`UPPERCASE WORDS 123`,
		`0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec@1_0`;


		val kotlinIdentifier
			get() = when (this) {
				`lowercase words 123`,
				`UPPERCASE WORDS 123`,
				`0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec@1_0` ->
					"`$name`"

				else ->
					name
			}


		override fun toString() = when (this) {
			lowerCamelCase123 -> "lowerCamelCase123.toString()"
			lower_snake_case123 -> "lower_snake_case123.toString()"
			lowercase123 -> "lowercase123.toString()"
			`lowercase words 123` -> "lowercase words 123.toString()"
			UpperCamelCase123 -> "UpperCamelCase123.toString()"
			UPPER_SNAKE_CASE123 -> "UPPER_SNAKE_CASE123.toString()"
			UPPERCASE123 -> "UPPERCASE123.toString()"
			`UPPERCASE WORDS 123` -> "UPPERCASE WORDS 123.toString()"
			`0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec@1_0` ->
				"0 some spaces upperCase wtf snakes_are_coming_in 123 abc123 justHTML xml XML JSONCodec/1.0.toString()"
		}
	}
}
