package com.github.fluidsonic.fluid.json

import java.io.Reader


interface JSONParser {

	fun parse(source: JSONReader): Any?


	fun parse(source: Reader) =
		parse(JSONReader(source))


	fun parse(source: String) =
		parse(JSONReader(source))


	fun parseList(source: JSONReader): List<*> {
		val list = parse(source)
		if (list !is List<*>) {
			throw JSONException("expected a list, got ${list?.javaClass}")
		}

		return list
	}


	fun parseList(source: Reader) =
		parseList(JSONReader(source))


	fun parseList(source: String) =
		parseList(JSONReader(source))


	fun parseMap(source: JSONReader): Map<String, *> {
		val map = parse(source)
		if (map !is Map<*, *>) {
			throw JSONException("expected a map, got ${map?.javaClass}")
		}

		@Suppress("UNCHECKED_CAST")
		return map as Map<String, *>
	}


	fun parseMap(source: Reader) =
		parseMap(JSONReader(source))


	fun parseMap(source: String) =
		parseMap(JSONReader(source))


	companion object {

		operator fun invoke(): JSONParser =
			StandardParser.default
	}
}
