package com.github.fluidsonic.fluid.json


@Suppress("UNCHECKED_CAST")
internal class StandardParser : JSONParser {

	override fun parse(source: JSONReader) =
		source.use { parse(source = source, expectedType = null) }


	private fun parse(source: JSONReader, expectedType: ExpectedType?): Any? {
		when (expectedType) {
			ExpectedType.list ->
				if (source.nextToken != JSONToken.listStart)
					throw JSONException("expected a list, got ${source.nextToken}")

			ExpectedType.map ->
				if (source.nextToken != JSONToken.mapStart)
					throw JSONException("expected a map, got ${source.nextToken}")
		}

		var currentList: MutableList<Any?>? = null
		var currentKey: String? = null
		var currentMap: MutableMap<Any?, Any?>? = null
		val parents = mutableListOf<Any>()
		val parentKeys = mutableListOf<String>()
		var value: Any? = null

		loop@ while (true) {
			value = when (source.nextToken) {
				JSONToken.booleanValue ->
					source.readBoolean()

				JSONToken.listEnd -> {
					source.readListEnd()

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
					source.readListStart()

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
				-> source.readNull()

				JSONToken.mapEnd -> {
					source.readMapEnd()

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
					currentKey = source.readMapKey()
					continue@loop
				}

				JSONToken.mapStart -> {
					source.readMapStart()

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
					source.readNumber()

				JSONToken.stringValue ->
					source.readString()

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


	override fun parseList(source: JSONReader) =
		parse(source, expectedType = ExpectedType.list) as List<Any?>


	override fun parseMap(source: JSONReader) =
		parse(source, expectedType = ExpectedType.map) as Map<String, *>


	private enum class ExpectedType {
		map,
		list
	}


	companion object {

		val default = StandardParser()
	}
}
