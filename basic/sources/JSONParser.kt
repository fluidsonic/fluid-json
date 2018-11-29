package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONParser {

	fun parseList(source: JSONReader, finalizeAndClose: Boolean = true) =
		parseValueAsType<List<*>>(source, finalizeAndClose = finalizeAndClose)


	fun parseMap(source: JSONReader, finalizeAndClose: Boolean = true) =
		parseValueAsType<Map<String, *>>(source, finalizeAndClose = finalizeAndClose)


	private inline fun <reified ReturnValue> parseValueAsType(source: JSONReader, finalizeAndClose: Boolean): ReturnValue {
		val value = parseValueOrNull(source, finalizeAndClose = finalizeAndClose)
		return value as? ReturnValue
			?: throw JSONException("cannot parse ${ReturnValue::class}, got " + value?.let { "${value::class}: $value" })
	}


	fun parseValueOrNull(source: JSONReader, finalizeAndClose: Boolean = true): Any?


	companion object {

		val default: JSONParser get() = StandardParser
	}
}


fun JSONParser.parseList(source: Reader, finalizeAndClose: Boolean = true) =
	parseList(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONParser.parseList(source: String) =
	parseList(JSONReader.build(source))


fun JSONParser.parseMap(source: Reader, finalizeAndClose: Boolean = true) =
	parseMap(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONParser.parseMap(source: String) =
	parseMap(JSONReader.build(source))


fun JSONParser.parseValue(source: JSONReader, finalizeAndClose: Boolean = true) =
	parseValueOrNull(source, finalizeAndClose = finalizeAndClose) ?: throw JSONException("Unexpected null value at top-level")


fun JSONParser.parseValue(source: Reader, finalizeAndClose: Boolean = true) =
	parseValue(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONParser.parseValue(source: String) =
	parseValue(JSONReader.build(source))


fun JSONParser.parseValueOrNull(source: Reader, finalizeAndClose: Boolean = true) =
	parseValueOrNull(JSONReader.build(source), finalizeAndClose = finalizeAndClose)


fun JSONParser.parseValueOrNull(source: String) =
	parseValueOrNull(JSONReader.build(source))
