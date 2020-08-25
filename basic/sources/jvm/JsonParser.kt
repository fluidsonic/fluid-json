package io.fluidsonic.json

import java.io.*


public interface JsonParser {

	public fun parseList(source: JsonReader, withTermination: Boolean = true): List<*> =
		parseValueAsType(source, withTermination = withTermination)


	public fun parseMap(source: JsonReader, withTermination: Boolean = true): Map<String, *> =
		parseValueAsType(source, withTermination = withTermination)


	private inline fun <reified ReturnValue> parseValueAsType(source: JsonReader, withTermination: Boolean): ReturnValue {
		val value = parseValueOrNull(source, withTermination = withTermination)
		return value as? ReturnValue
			?: throw JsonException.Schema(
				message = "Cannot parse ${ReturnValue::class}, got " + value?.let { "${value::class}: $value" },
				offset = source.offset,
				path = source.path
			)
	}


	public fun parseValueOrNull(source: JsonReader, withTermination: Boolean = true): Any?


	public companion object {

		public val default: JsonParser get() = StandardParser
	}
}


public fun JsonParser.parseList(source: Reader, withTermination: Boolean = true): List<*> =
	parseList(JsonReader.build(source), withTermination = withTermination)


public fun JsonParser.parseList(source: String): List<*> =
	parseList(JsonReader.build(source))


public fun JsonParser.parseMap(source: Reader, withTermination: Boolean = true): Map<String, *> =
	parseMap(JsonReader.build(source), withTermination = withTermination)


public fun JsonParser.parseMap(source: String): Map<String, *> =
	parseMap(JsonReader.build(source))


public fun JsonParser.parseValue(source: JsonReader, withTermination: Boolean = true): Any =
	parseValueOrNull(source, withTermination = withTermination) ?: throw JsonException.Schema("Unexpected null value at top-level")


public fun JsonParser.parseValue(source: Reader, withTermination: Boolean = true): Any =
	parseValue(JsonReader.build(source), withTermination = withTermination)


public fun JsonParser.parseValue(source: String): Any =
	parseValue(JsonReader.build(source))


public fun JsonParser.parseValueOrNull(source: Reader, withTermination: Boolean = true): Any? =
	parseValueOrNull(JsonReader.build(source), withTermination = withTermination)


public fun JsonParser.parseValueOrNull(source: String): Any? =
	parseValueOrNull(JsonReader.build(source))
