package com.github.fluidsonic.fluid.json

import java.io.Reader


internal interface JSONParser {

	fun parse(reader: JSONReader): Any?


	fun parse(reader: Reader) =
		parse(JSON.reader(reader))


	fun parse(string: String) =
		parse(JSON.reader(string))


	fun parseList(reader: JSONReader): List<*> {
		val list = parse(reader)
		if (list !is List<*>) {
			throw JSONException("expected a list, got ${list?.javaClass}")
		}

		return list
	}


	fun parseList(reader: Reader) =
		parseList(JSON.reader(reader))


	fun parseList(string: String) =
		parseList(JSON.reader(string))


	fun parseMap(reader: JSONReader): Map<String, *> {
		val map = parse(reader)
		if (map !is Map<*, *>) {
			throw JSONException("expected a map, got ${map?.javaClass}")
		}

		@Suppress("UNCHECKED_CAST")
		return map as Map<String, *>
	}


	fun parseMap(reader: Reader) =
		parseMap(JSON.reader(reader))


	fun parseMap(string: String) =
		parseMap(JSON.reader(string))
}
