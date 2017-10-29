package com.github.fluidsonic.fluid.json


internal object NonRecursiveParser {

	@Suppress("UNCHECKED_CAST")
	fun parse(source: JSONReader): Any? {
		var currentList: MutableList<Any?>? = null
		var currentKey: String? = null
		var currentMap: MutableMap<Any?, Any?>? = null
		val parents = mutableListOf<Any>()
		val parentKeys = mutableListOf<String>()

		loop@ while (true) {
			val value: Any? = when (source.nextToken) {
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
					throw IllegalStateException("reader is messed up")
			}

			when {
				currentList != null -> currentList.add(value)
				currentMap != null -> currentMap[currentKey] = value
				else -> return value ?: throw IllegalStateException("reader is messed up")
			}
		}
	}


}
