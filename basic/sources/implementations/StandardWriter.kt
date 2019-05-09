package com.github.fluidsonic.fluid.json

import java.io.*


internal class StandardWriter(private val destination: Writer)
	: JSONWriter, Closeable, Flushable by destination {

	private var isClosed = false
	private var state = State()
	private val stateCache: MutableList<State> = mutableListOf()
	private val stateStack: MutableList<State> = mutableListOf(state)

	override var isErrored = false
		private set


	override fun close() {
		if (isClosed) return

		isClosed = true
		destination.close()
	}


	override fun beginValueIsolation(): JSONDepth {
		ensureNotClosed()

		valueIsolationCheck(state.tokenLocation != TokenLocation.afterRootValue) { "the root value has already been written" }
		valueIsolationCheck(!state.isInValueIsolation || !state.hasWrittenIsolatedValue) { "cannot begin before previous one has ended" }

		state.valueIsolationCount += 1

		return depth
	}


	override val depth
		get() = JSONDepth(stateStack.size - 1)


	private fun didWriteValue() {
		state.tokenLocation = state.tokenLocation.afterValue
			?: serializationError("Internal inconsistency: unexpected token location '${state.tokenLocation}' after writing value")

		@Suppress("NON_EXHAUSTIVE_WHEN")
		when (state.tokenLocation) {
			TokenLocation.afterListElement ->
				state.currentValueListIndex += 1

			TokenLocation.afterMapElement ->
				state.currentValueMapKey = null
		}

		if (state.isInValueIsolation) {
			state.hasWrittenIsolatedValue = true
		}
	}


	override fun endValueIsolation(depth: JSONDepth) {
		ensureNotClosed()

		valueIsolationCheck(depth <= this.depth) { "lists or maps have been ended prematurely" }
		valueIsolationCheck(this.depth <= depth) { "lists or maps have not been ended properly" }
		valueIsolationCheck(isInValueIsolation) { "cannot end isolation - it either hasn't begun or been ended already" }
		valueIsolationCheck(state.hasWrittenIsolatedValue) { "exactly one value has been expected but none was written" }

		val valueIsolationCount = state.valueIsolationCount - 1
		state.valueIsolationCount = valueIsolationCount

		if (valueIsolationCount == 0) {
			state.hasWrittenIsolatedValue = false
		}
	}


	private fun ensureNotClosed() {
		serializationCheck(!isClosed) { "Cannot operate on a closed JSONWriter" }
	}


	override val isInValueIsolation
		get() = state.isInValueIsolation


	override fun markAsErrored() {
		if (isClosed) return

		isErrored = true
	}


	override val path: JSONPath
		get() {
			if (isClosed) return JSONPath.root

			return when (state.tokenLocation) {
				TokenLocation.afterRootValue,
				TokenLocation.beforeRootValue ->
					JSONPath.root

				else ->
					JSONPath(elements = stateStack.mapNotNull { it.toPathElement() })
			}
		}


	private fun popState() {
		valueIsolationCheck(!state.isInValueIsolation || state.hasWrittenIsolatedValue) { "cannot end a list or map since a value is still being expected" }
		valueIsolationCheck(!state.isInValueIsolation || !state.hasWrittenIsolatedValue) { "list or map is being ended prematurely" }

		stateCache += stateStack.removeAt(stateStack.size - 1)
		state = stateStack.last()
	}


	private fun pushState(tokenLocation: TokenLocation) {
		serializationCheck(tokenLocation.isBeforeValue) { "Internal inconsistency: cannot push state except at the beginning of a value" }

		val newState = if (stateCache.isNotEmpty()) stateCache.removeAt(stateCache.size - 1) else State()
		newState.reset(tokenLocation = tokenLocation)

		state = newState
		stateStack += newState
	}


	private inline fun serializationCheck(value: Boolean, lazyMessage: () -> String) {
		// contract {
		//  returns() implies value
		// }

		if (!value) serializationError(lazyMessage())
	}


	private fun serializationError(message: String): Nothing =
		throw JSONException.Serialization(message = message, path = path)


	override fun terminate() {
		ensureNotClosed()
		close()

		withErrorChecking {
			if (state.tokenLocation != TokenLocation.afterRootValue)
				serializationError("JSONWriter was terminated without writing a complete value")
		}
	}


	private inline fun valueIsolationCheck(value: Boolean, lazyMessage: () -> String) {
		// contract {
		//  returns() implies value
		// }

		if (!value) valueIsolationError(lazyMessage())
	}


	private fun valueIsolationError(message: String): Nothing {
		throw JSONException.Serialization(
			message = "Value isolation failed: $message",
			path = path
		)
	}


	private fun willWriteValue(isString: Boolean) {
		ensureNotClosed()

		valueIsolationCheck(!state.isInValueIsolation || !state.hasWrittenIsolatedValue) { "cannot write more than one value in a context where only one is expected" }

		when (state.tokenLocation) {
			TokenLocation.afterListElement ->
				destination.write(JSONCharacter.Symbol.comma)

			TokenLocation.afterListStart ->
				Unit

			TokenLocation.afterMapElement -> {
				serializationCheck(isString) { "Expected a string as map key" }

				destination.write(JSONCharacter.Symbol.comma)
			}

			TokenLocation.afterMapKey ->
				destination.write(JSONCharacter.Symbol.colon)

			TokenLocation.afterMapStart ->
				serializationCheck(isString) { "Expected a string as map key" }

			TokenLocation.afterRootValue ->
				serializationError("Cannot write more than one value a the JSON root")

			TokenLocation.beforeRootValue ->
				Unit
		}
	}


	override fun writeBoolean(value: Boolean) {
		writeValue(isString = false) {
			destination.write(if (value) "true" else "false")
		}
	}


	override fun writeByte(value: Byte) {
		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeDouble(value: Double) {
		serializationCheck(value.isFinite()) { "Cannot write double value '$value'" }

		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeFloat(value: Float) {
		serializationCheck(value.isFinite()) { "Cannot write float value '$value'" }

		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeInt(value: Int) {
		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeLong(value: Long) {
		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeListEnd() {
		ensureNotClosed()

		withErrorChecking {
			when (state.tokenLocation) {
				TokenLocation.afterListStart,
				TokenLocation.afterListElement ->
					Unit

				else ->
					serializationError("Cannot write end of list when not in a list")
			}

			destination.write(JSONCharacter.Symbol.rightSquareBracket)

			popState()
			didWriteValue()
		}
	}


	override fun writeListStart() {
		withErrorChecking {
			willWriteValue(isString = false)

			destination.write(JSONCharacter.Symbol.leftSquareBracket)

			pushState(tokenLocation = TokenLocation.afterListStart)

			state.currentValueListIndex = 0
		}
	}


	override fun writeMapEnd() {
		ensureNotClosed()

		withErrorChecking {
			when (state.tokenLocation) {
				TokenLocation.afterMapStart,
				TokenLocation.afterMapElement ->
					Unit

				TokenLocation.afterMapKey ->
					serializationError("Cannot write end of map right after a key, value expected instead")

				else ->
					serializationError("Cannot write end of map when not in a map")
			}

			destination.write(JSONCharacter.Symbol.rightCurlyBracket)

			popState()
			didWriteValue()
		}
	}


	override fun writeMapStart() {
		withErrorChecking {
			willWriteValue(isString = false)

			destination.write(JSONCharacter.Symbol.leftCurlyBracket)

			pushState(tokenLocation = TokenLocation.afterMapStart)
		}
	}


	override fun writeNull() {
		writeValue(isString = false) {
			destination.write("null")
		}
	}


	override fun writeShort(value: Short) {
		writeValue(isString = false) {
			destination.write(value.toString())
		}
	}


	override fun writeString(value: String) {
		writeValue(isString = true) {
			@Suppress("NON_EXHAUSTIVE_WHEN")
			when (state.tokenLocation) {
				TokenLocation.afterMapElement,
				TokenLocation.afterMapStart ->
					state.currentValueMapKey = value
			}

			// TODO optimize
			destination.write(JSONCharacter.Symbol.quotationMark)

			for (character in value) {
				when (character) {
					'"', '\\' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(character.toInt())
					}

					'\b' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.b)
					}

					'\u000C' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.f)
					}

					'\n' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.n)
					}

					'\r' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.r)
					}

					'\t' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.t)
					}

					'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
					'\u000B', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014',
					'\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B', '\u001C',
					'\u001D', '\u001E', '\u001F' -> {
						destination.write(JSONCharacter.Symbol.reverseSolidus)
						destination.write(JSONCharacter.Letter.u)
						destination.write(JSONCharacter.Digit.zero)
						destination.write(JSONCharacter.Digit.zero)

						if (character.toInt() >= 0x10) {
							destination.write(JSONCharacter.Digit.one)
						}
						else {
							destination.write(JSONCharacter.Digit.zero)
						}

						destination.write(hexCharacters[character.toInt() and 0xF].toInt())
					}

					else ->
						destination.write(character.toInt())
				}
			}

			destination.write(JSONCharacter.Symbol.quotationMark)
		}
	}


	private inline fun writeValue(isString: Boolean, crossinline write: () -> Unit) {
		withErrorChecking {
			willWriteValue(isString = isString)
			write()
			didWriteValue()
		}
	}


	private companion object {

		val hexCharacters = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
	}


	private class State {

		var currentValueListIndex = -1
		var currentValueMapKey: String? = null
		var hasWrittenIsolatedValue = false
		var tokenLocation = TokenLocation.beforeRootValue
		var valueIsolationCount = 0


		val isInValueIsolation
			get() = valueIsolationCount > 0


		fun reset(tokenLocation: TokenLocation) {
			this.tokenLocation = tokenLocation

			currentValueListIndex = -1
			currentValueMapKey = null
			hasWrittenIsolatedValue = false
			valueIsolationCount = 0
		}


		fun toPathElement(): JSONPath.Element? {
			if (currentValueListIndex >= 0) return JSONPath.Element.ListIndex(currentValueListIndex)
			currentValueMapKey?.let { return JSONPath.Element.MapKey(it) }

			return null
		}
	}


	private enum class TokenLocation {

		afterListElement {
			override val afterValue get() = afterListElement
		},
		afterListStart {
			override val afterValue get() = afterListElement
		},
		afterMapElement {
			override val afterValue get() = afterMapKey
		},
		afterMapKey {
			override val afterValue get() = afterMapElement
		},
		afterMapStart {
			override val afterValue get() = afterMapKey
		},
		afterRootValue,
		beforeRootValue {
			override val afterValue get() = afterRootValue
		};


		open val afterValue: TokenLocation? get() = null


		val isBeforeValue
			get() = (afterValue != null)
	}
}
