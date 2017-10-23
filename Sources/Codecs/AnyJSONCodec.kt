package com.github.fluidsonic.fluid.json


object AnyJSONCodec : JSONCodec<Any, JSONCoderContext> {

	@Suppress("UNCHECKED_CAST")
	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Any {
		var currentList: MutableList<Any?>? = null
		var currentKey: String? = null
		var currentMap: MutableMap<Any?, Any?>? = null
		val parents = mutableListOf<Any>()
		val parentKeys = mutableListOf<String>()

		loop@ while (true) {
			val value: Any? = when (decoder.nextToken) {
				JSONToken.booleanValue ->
					decoder.readBoolean()

				JSONToken.listEnd -> {
					decoder.readListEnd()

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
					decoder.readListStart()

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
				-> decoder.readNull()

				JSONToken.mapEnd -> {
					decoder.readMapEnd()

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
					currentKey = decoder.readMapKey()
					continue@loop
				}

				JSONToken.mapStart -> {
					decoder.readMapStart()

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
					decoder.readNumber()

				JSONToken.stringValue ->
					decoder.readString()

				else ->
					throw IllegalStateException("decoder is messed up")
			}

			when {
				currentList != null -> currentList.add(value)
				currentMap != null -> currentMap[currentKey] = value
				else -> return value ?: throw IllegalStateException("decoder is messed up")
			}
		}
	}


	override fun encode(value: Any, encoder: JSONEncoder<out JSONCoderContext>) {
		var currentIterator: Iterator<*>? = null
		var currentValue: Any? = value
		var isInMap = false
		val iteratorStack = mutableListOf<Iterator<*>>()
		val isInMapStack = mutableListOf<Boolean>()
		var isInMapElementValue = false

		loop@ do {
			if (!isInMapElementValue && currentIterator != null) {
				if (currentIterator.hasNext()) {
					currentValue = currentIterator.next()
				}
				else {
					if (isInMap) {
						encoder.writeMapEnd()
					}
					else {
						encoder.writeListEnd()
					}

					val iteratorStackSize = iteratorStack.size
					if (iteratorStackSize > 0) {
						currentIterator = iteratorStack.removeAt(iteratorStackSize - 1)
						isInMap = isInMapStack.removeAt(isInMapStack.size - 1)

						continue
					}
					else {
						break
					}
				}
			}

			currentValue = when (currentValue) {
				is Map<*, *> -> currentValue
				is Iterable<*> -> currentValue.iterator()
				is Sequence<*> -> currentValue.iterator()
				is Array<*> -> currentValue.iterator()
				is BooleanArray -> currentValue.iterator()
				is ByteArray -> currentValue.iterator()
				is DoubleArray -> currentValue.iterator()
				is FloatArray -> currentValue.iterator()
				is IntArray -> currentValue.iterator()
				is LongArray -> currentValue.iterator()
				is ShortArray -> currentValue.iterator()
				else -> currentValue
			}

			when (currentValue) {
				null ->
					encoder.writeNull()

				is Boolean ->
					encoder.writeBoolean(currentValue)

				is Byte ->
					encoder.writeByte(currentValue)

				is Double ->
					when {
						currentValue.isFinite() -> encoder.writeDouble(currentValue)
						else -> throw JSONException("Cannot serialize double value '$currentValue'")
					}

				is Float ->
					when {
						currentValue.isFinite() -> encoder.writeFloat(currentValue)
						else -> throw JSONException("Cannot serialize float value '$currentValue'")
					}

				is Int ->
					encoder.writeInt(currentValue)

				is Iterator<*> -> { //
					if (currentIterator != null) {
						iteratorStack += currentIterator
					}
					isInMapStack += isInMap

					currentIterator = currentValue
					isInMap = false

					encoder.writeListStart()
				}

				is Long ->
					encoder.writeLong(currentValue)

				is Short ->
					encoder.writeShort(currentValue)

				is String ->
					encoder.writeString(currentValue)

				is Map<*, *> -> {
					if (currentIterator != null) {
						iteratorStack += currentIterator
					}
					isInMapStack += isInMap

					currentIterator = currentValue.iterator()
					isInMap = true

					encoder.writeMapStart()
				}

				is Map.Entry<*, *> ->
					if (isInMap && !isInMapElementValue) {
						val (elementKey, elementValue) = currentValue
						when (elementKey) {
							is String -> encoder.writeMapKey(elementKey)
							else -> throw JSONException("Cannot serialize non-String key of ${currentValue::class}: $currentValue")
						}

						currentValue = elementValue
						isInMapElementValue = true
						continue@loop
					}
					else
						throw JSONException("Cannot serialize value of ${currentValue::class}: $currentValue")

				is Number -> // after subclasses
					encoder.writeNumber(currentValue)

				else ->
					throw JSONException("Cannot serialize value of ${currentValue::class}: $currentValue")
			}

			isInMapElementValue = false
		}
		while (currentIterator != null)
	}


	override val encodableClasses = setOf(
		Array<Any?>::class,
		Boolean::class,
		BooleanArray::class,
		Byte::class,
		ByteArray::class,
		Double::class,
		DoubleArray::class,
		Float::class,
		FloatArray::class,
		Int::class,
		IntArray::class,
		Iterable::class,
		Long::class,
		LongArray::class,
		Map::class,
		Number::class,
		Sequence::class,
		Short::class,
		ShortArray::class,
		String::class
	)


	override val decodableClass = Any::class
}
