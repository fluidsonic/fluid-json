package io.fluidsonic.json

import java.io.*


interface JsonParser {

	fun parseList(source: JsonReader, withTermination: Boolean = true) =
		parseValueAsType<List<*>>(source, withTermination = withTermination)


	fun parseMap(source: JsonReader, withTermination: Boolean = true) =
		parseValueAsType<Map<String, *>>(source, withTermination = withTermination)


	private inline fun <reified ReturnValue> parseValueAsType(source: JsonReader, withTermination: Boolean): ReturnValue {
		val value = parseValueOrNull(source, withTermination = withTermination)
		return value as? ReturnValue
			?: throw JsonException.Schema(
				message = "Cannot parse ${ReturnValue::class}, got " + value?.let { "${value::class}: $value" },
				offset = source.offset,
				path = source.path
			)
	}


	fun parseValueOrNull(source: JsonReader, withTermination: Boolean = true): Any?


	companion object {

		val default: JsonParser get() = StandardParser
	}
}


fun JsonParser.parseList(source: Reader, withTermination: Boolean = true) =
	parseList(JsonReader.build(source), withTermination = withTermination)


fun JsonParser.parseList(source: String) =
	parseList(JsonReader.build(source))


fun JsonParser.parseMap(source: Reader, withTermination: Boolean = true) =
	parseMap(JsonReader.build(source), withTermination = withTermination)


fun JsonParser.parseMap(source: String) =
	parseMap(JsonReader.build(source))


fun JsonParser.parseValue(source: JsonReader, withTermination: Boolean = true) =
	parseValueOrNull(source, withTermination = withTermination) ?: throw JsonException.Schema("Unexpected null value at top-level")


fun JsonParser.parseValue(source: Reader, withTermination: Boolean = true) =
	parseValue(JsonReader.build(source), withTermination = withTermination)


fun JsonParser.parseValue(source: String) =
	parseValue(JsonReader.build(source))


fun JsonParser.parseValueOrNull(source: Reader, withTermination: Boolean = true) =
	parseValueOrNull(JsonReader.build(source), withTermination = withTermination)


fun JsonParser.parseValueOrNull(source: String) =
	parseValueOrNull(JsonReader.build(source))
