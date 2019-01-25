package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONParser {

	fun parseList(source: JSONReader, withTermination: Boolean = true) =
		parseValueAsType<List<*>>(source, withTermination = withTermination)


	fun parseMap(source: JSONReader, withTermination: Boolean = true) =
		parseValueAsType<Map<String, *>>(source, withTermination = withTermination)


	private inline fun <reified ReturnValue> parseValueAsType(source: JSONReader, withTermination: Boolean): ReturnValue {
		val value = parseValueOrNull(source, withTermination = withTermination)
		return value as? ReturnValue
			?: throw JSONException.Schema(
				message = "Cannot parse ${ReturnValue::class}, got " + value?.let { "${value::class}: $value" },
				offset = source.offset,
				path = source.path
			)
	}


	fun parseValueOrNull(source: JSONReader, withTermination: Boolean = true): Any?


	companion object {

		val default: JSONParser get() = StandardParser
	}
}


fun JSONParser.parseList(source: Reader, withTermination: Boolean = true) =
	parseList(JSONReader.build(source), withTermination = withTermination)


fun JSONParser.parseList(source: String) =
	parseList(JSONReader.build(source))


fun JSONParser.parseMap(source: Reader, withTermination: Boolean = true) =
	parseMap(JSONReader.build(source), withTermination = withTermination)


fun JSONParser.parseMap(source: String) =
	parseMap(JSONReader.build(source))


fun JSONParser.parseValue(source: JSONReader, withTermination: Boolean = true) =
	parseValueOrNull(source, withTermination = withTermination) ?: throw JSONException.Schema("Unexpected null value at top-level")


fun JSONParser.parseValue(source: Reader, withTermination: Boolean = true) =
	parseValue(JSONReader.build(source), withTermination = withTermination)


fun JSONParser.parseValue(source: String) =
	parseValue(JSONReader.build(source))


fun JSONParser.parseValueOrNull(source: Reader, withTermination: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), withTermination = withTermination)


fun JSONParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))
