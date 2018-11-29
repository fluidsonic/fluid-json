package com.github.fluidsonic.fluid.json


internal object StandardSerializer : JSONSerializer {

	override fun serializeValue(value: Any?, destination: JSONWriter, withTermination: Boolean) {
		destination.withTermination(withTermination) { serializeValueUnterminated(value, destination) }
	}


	private fun serializeValueUnterminated(value: Any?, destination: JSONWriter) {
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
						destination.writeMapEnd()
					}
					else {
						destination.writeListEnd()
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
					destination.writeNull()

				is Boolean ->
					destination.writeBoolean(currentValue)

				is Byte ->
					destination.writeByte(currentValue)

				is Double ->
					when {
						currentValue.isFinite() -> destination.writeDouble(currentValue)
						else -> throw JSONException("Cannot serialize double value '$currentValue'")
					}

				is Float ->
					when {
						currentValue.isFinite() -> destination.writeFloat(currentValue)
						else -> throw JSONException("Cannot serialize float value '$currentValue'")
					}

				is Int ->
					destination.writeInt(currentValue)

				is Iterator<*> -> { //
					if (currentIterator != null) {
						iteratorStack += currentIterator
					}
					isInMapStack += isInMap

					currentIterator = currentValue
					isInMap = false

					destination.writeListStart()
				}

				is Long ->
					destination.writeLong(currentValue)

				is Short ->
					destination.writeShort(currentValue)

				is String ->
					destination.writeString(currentValue)

				is Map<*, *> -> {
					if (currentIterator != null) {
						iteratorStack += currentIterator
					}
					isInMapStack += isInMap

					currentIterator = currentValue.iterator()
					isInMap = true

					destination.writeMapStart()
				}

				is Map.Entry<*, *> ->
					if (isInMap && !isInMapElementValue) {
						val (elementKey, elementValue) = currentValue
						when (elementKey) {
							is String -> destination.writeMapKey(elementKey)
							else -> throw JSONException("Cannot serialize non-String key of ${currentValue::class}: $currentValue")
						}

						currentValue = elementValue
						isInMapElementValue = true
						continue@loop
					}
					else
						throw JSONException("Cannot serialize value of ${currentValue::class}: $currentValue")

				is Number -> // after subclasses
					destination.writeNumber(currentValue)

				else ->
					throw JSONException("Cannot serialize value of ${currentValue::class}: $currentValue")
			}

			isInMapElementValue = false
		}
		while (currentIterator != null)
	}
}
