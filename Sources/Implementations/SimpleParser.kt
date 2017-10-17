package com.github.fluidsonic.fluid.json


@Suppress("UNCHECKED_CAST")
internal object SimpleParser : JSONParser {

	override fun parse(reader: JSONReader) =
		parse(reader, expectedType = null)


	private fun parse(reader: JSONReader, expectedType: ExpectedType?): Any? {
		when (expectedType) {
			ExpectedType.list ->
				if (reader.nextToken != JSONToken.listStart)
					throw JSONException("expected a list, got ${reader.nextToken}")

			ExpectedType.map ->
				if (reader.nextToken != JSONToken.mapStart)
					throw JSONException("expected a map, got ${reader.nextToken}")
		}

		var currentList: MutableList<Any?>? = null
		var currentKey: String? = null
		var currentMap: MutableMap<Any?, Any?>? = null
		val parents = mutableListOf<Any>()
		val parentKeys = mutableListOf<String>()
		var value: Any? = null

		loop@ while (true) {
			value = when (reader.nextToken) {
				JSONToken.booleanValue ->
					reader.readBoolean()

				JSONToken.listEnd -> {
					reader.readListEnd()

					val list = currentList

					val parentCount = parents.size
					if (parentCount > 0) {
						val parent = parents.removeAt(parentCount - 1)
						if (parent is MutableList<*>) {
							currentList = parent as MutableList<Any?>
						}
						else {
							currentList = null
							currentMap = parent as MutableMap<Any?, Any?>
						}
					}
					else {
						currentList = null
						currentMap = null
					}

					list
				}

				JSONToken.listStart -> {
					reader.readListStart()

					val list = mutableListOf<Any?>()
					if (currentList != null) {
						parents += currentList
					}
					else if (currentMap != null) {
						parents += currentMap
						currentMap = null
					}

					currentList = list
					continue@loop
				}

				JSONToken.nullValue
				-> reader.readNull()

				JSONToken.mapEnd -> {
					reader.readMapEnd()

					val map = currentMap

					val parentCount = parents.size
					if (parentCount > 0) {
						val parent = parents.removeAt(parentCount - 1)
						if (parent is MutableMap<*, *>) {
							currentMap = parent as MutableMap<Any?, Any?>
						}
						else {
							currentList = parent as MutableList<Any?>
							currentMap = null
						}
					}
					else {
						currentList = null
						currentMap = null
					}

					val parentKeyCount = parentKeys.size
					if (parentKeyCount > 0) {
						currentKey = parentKeys.removeAt(parentKeyCount - 1)
					}

					map
				}

				JSONToken.mapKey -> {
					currentKey = reader.readMapKey()
					continue@loop
				}

				JSONToken.mapStart -> {
					reader.readMapStart()

					val map = mutableMapOf<Any?, Any?>()
					if (currentMap != null) {
						parents += currentMap
					}
					else if (currentList != null) {
						parents += currentList
						currentList = null
					}

					if (currentKey != null) {
						parentKeys += currentKey
						currentKey = null
					}

					currentMap = map
					continue@loop
				}

				JSONToken.numberValue ->
					reader.readNumber()

				JSONToken.stringValue ->
					reader.readString()

				else ->
					return value
			}

			when {
				currentList != null -> currentList.add(value)
				currentMap != null -> currentMap[currentKey] = value
				else -> assert(false)
			}
		}
	}


	override fun parseList(reader: JSONReader) =
		parse(reader, expectedType = ExpectedType.list) as List<Any?>


	override fun parseMap(reader: JSONReader) =
		parse(reader, expectedType = ExpectedType.map) as Map<String, *>


	private enum class ExpectedType {
		map,
		list
	}
}
