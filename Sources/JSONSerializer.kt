package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
class JSONSerializer(
	@API(status = API.Status.EXPERIMENTAL)
	val convertsInvalidKeysToString: Boolean = false,

	@API(status = API.Status.EXPERIMENTAL)
	val convertsInvalidValuesToString: Boolean = false
) {

	@API(status = API.Status.EXPERIMENTAL)
	fun serialize(value: Any?) =
		buildString { serialize(value, output = this) }


	@API(status = API.Status.EXPERIMENTAL)
	fun serialize(value: Any?, output: StringBuilder) {
		JSONPathBuildingException.track {
			serializeAny(value, output = output)
		}
	}


	private fun serializeAny(value: Any?, output: StringBuilder) {
		when (value) {
			null -> serializeNull(output = output)
			is Boolean -> serializeBoolean(value, output = output)
			is Byte -> serializeByte(value, output = output)
			is Double -> serializeDouble(value, output = output)
			is Float -> serializeFloat(value, output = output)
			is Int -> serializeInt(value, output = output)
			is Long -> serializeLong(value, output = output)
			is Short -> serializeShort(value, output = output)
			is String -> serializeString(value, output = output)
			is Map<*, *> -> serializeMap(value, output = output)
			is Iterable<*> -> serializeIterable(value, output = output)
			else -> serializeInvalid(value, output = output)
		}
	}


	private fun serializeBoolean(value: Boolean, output: StringBuilder) {
		output.append(value)
	}


	private fun serializeByte(value: Byte, output: StringBuilder) {
		output.append(value.toInt())
	}


	private fun serializeDouble(value: Double, output: StringBuilder) {
		if (value.isFinite()) {
			output.append(value)
		}
		else {
			convertsInvalidValuesToString || throw invalidValue(value)

			serializeString(value.toString(), output = output)
		}
	}


	private fun serializeFloat(value: Float, output: StringBuilder) {
		if (value.isFinite()) {
			output.append(value)
		}
		else {
			convertsInvalidValuesToString || throw invalidValue(value)

			serializeString(value.toString(), output = output)
		}
	}


	private fun serializeInt(value: Int, output: StringBuilder) {
		output.append(value)
	}


	private fun serializeInvalid(value: Any, output: StringBuilder) {
		convertsInvalidValuesToString || throw invalidValue(value)

		serializeString(value.toString(), output = output)
	}


	private fun serializeIterable(value: Iterable<*>, output: StringBuilder) {
		output.append('[')

		for ((index, childValue) in value.withIndex()) {
			if (index > 0) {
				output.append(',')
			}

			JSONPathBuildingException.track(index) {
				serializeAny(childValue, output = output)
			}
		}

		output.append(']')
	}


	private fun serializeLong(value: Long, output: StringBuilder) {
		output.append(value)
	}


	private fun serializeMap(value: Map<*, *>, output: StringBuilder) {
		output.append('{')

		var firstElement = true
		for ((childKey, childValue) in value) {
			if (firstElement) {
				firstElement = false
			}
			else {
				output.append(',')
			}

			serializeMapEntry(key = childKey, value = childValue, output = output)
		}

		output.append('}')
	}


	private fun serializeMapEntry(key: Any?, value: Any?, output: StringBuilder) {
		val name = when (key) {
			is String -> key
			else -> {
				convertsInvalidKeysToString || throw invalidKey(key)

				value.toString()
			}
		}

		JSONPathBuildingException.track(name) {
			serializeString(name, output = output)
			output.append(':')
			serializeAny(value, output = output)
		}
	}


	private fun serializeNull(output: StringBuilder) {
		output.append("null")
	}


	private fun serializeShort(value: Short, output: StringBuilder) {
		output.append(value.toInt())
	}


	private fun serializeString(value: String, output: StringBuilder) {
		val length = value.length

		output.ensureCapacity(output.length + length + 2)
		output.append('"')

		for (character in value) {
			when (character) {
				'"', '\\' -> {
					output.append('\\')
					output.append(character)
				}

				'\b' -> {
					output.append('\\')
					output.append('b')
				}

				'\u000C' -> {
					output.append('\\')
					output.append('f')
				}

				'\n' -> {
					output.append('\\')
					output.append('n')
				}

				'\r' -> {
					output.append('\\')
					output.append('r')
				}

				'\t' -> {
					output.append('\\')
					output.append('t')
				}

				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014',
				'\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B', '\u001C',
				'\u001D', '\u001E', '\u001F' -> {
					output.append('\\')
					output.append('u')
					output.append('0')
					output.append('0')

					if (character.toInt() >= 0x10) {
						output.append('1')
					}
					else {
						output.append('0')
					}

					output.append(hexCharacters[character.toInt() and 0xF])
				}

				else ->
					output.append(character)
			}
		}

		output.append('"')
	}


	private companion object {

		val hexCharacters = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')


		val Any?.debugString
			get() = when (this) {
				null -> "null"
				else -> "${this.javaClass}: $this"
			}


		fun invalidKey(value: Any?): Exception =
			JSONPathBuildingException("cannot serialize key: ${value.debugString}")


		fun invalidValue(value: Any?): Exception =
			JSONPathBuildingException("cannot serialize value: ${value.debugString}")
	}
}
