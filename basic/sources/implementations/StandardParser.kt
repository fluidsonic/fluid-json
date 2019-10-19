package io.fluidsonic.json


internal object StandardParser : JsonParser {

	private fun parse(source: JsonReader, expectedType: ExpectedType, withTermination: Boolean) =
		source.withTermination(withTermination) { parseUnterminated(source, expectedType) }


	override fun parseValueOrNull(source: JsonReader, withTermination: Boolean) =
		parse(source, ExpectedType.any, withTermination = withTermination)


	override fun parseList(source: JsonReader, withTermination: Boolean) =
		parse(source, ExpectedType.list, withTermination = withTermination) as List<*>


	@Suppress("UNCHECKED_CAST")
	override fun parseMap(source: JsonReader, withTermination: Boolean) =
		parse(source, ExpectedType.map, withTermination = withTermination) as Map<String, *>


	@Suppress("UNCHECKED_CAST")
	private fun parseUnterminated(source: JsonReader, expectedType: ExpectedType): Any? {
		var currentList: MutableList<Any?>? = null
		var currentKey: String? = null
		var currentMap: MutableMap<Any?, Any?>? = null
		val parents = mutableListOf<Any>()
		val parentKeys = mutableListOf<String>()

		when (expectedType) {
			ExpectedType.any -> Unit

			ExpectedType.list -> source.nextToken == JsonToken.listStart ||
				throw JsonException.Schema(
					message = "Expected a list, got ${source.nextToken}",
					offset = source.offset,
					path = source.path
				)

			ExpectedType.map -> source.nextToken == JsonToken.mapStart ||
				throw JsonException.Schema(
					message = "Expected a map, got ${source.nextToken}",
					offset = source.offset,
					path = source.path
				)
		}

		loop@ while (true) {
			val value: Any? = when (source.nextToken) {
				JsonToken.booleanValue ->
					source.readBoolean()

				JsonToken.listEnd -> {
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

				JsonToken.listStart -> {
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

				JsonToken.nullValue
				-> source.readNull()

				JsonToken.mapEnd -> {
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

				JsonToken.mapKey -> {
					currentKey = source.readMapKey()
					continue@loop
				}

				JsonToken.mapStart -> {
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

				JsonToken.numberValue ->
					source.readNumber()

				JsonToken.stringValue ->
					source.readString()

				else ->
					throw IllegalStateException("reader is messed up: ${source.nextToken}")
			}

			when {
				currentList != null -> currentList.add(value)
				currentMap != null -> currentMap[currentKey] = value
				else -> return value
			}
		}
	}


	private enum class ExpectedType {
		any, list, map
	}
}
