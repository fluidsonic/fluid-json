package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Flushable
import java.io.IOException
import java.io.Writer


internal class StandardWriter(private val destination: Writer)
	: JSONWriter, Closeable, Flushable by destination {

	private var state = State.initial
	private val stateStack = mutableListOf<State>()

	override var isErrored = false
		private set


	override fun close() {
		if (state == State.closed) return

		state = State.closed
		destination.close()
	}


	override fun markAsErrored() {
		if (state == State.closed) return

		isErrored = true
	}


	private fun startValue(isString: Boolean) {
		when (state) {
			State.afterListElement ->
				destination.write(JSONCharacter.Symbol.comma)

			State.afterListStart ->
				state = State.afterListElement

			State.afterMapElement ->
				if (isString) {
					destination.write(JSONCharacter.Symbol.comma)
					state = State.afterMapKey
				}
				else {
					throw JSONException("Expected a string as map key")
				}

			State.afterMapKey -> {
				destination.write(JSONCharacter.Symbol.colon)
				state = State.afterMapElement
			}

			State.afterMapStart ->
				if (isString) {
					state = State.afterMapKey
				}
				else {
					throw JSONException("Expected a string as map key")
				}

			State.closed ->
				throw IOException("Cannot operate on a closed writer")

			State.end ->
				throw JSONException("Cannot write more than one value a the JSON root")

			State.initial ->
				state = State.end
		}
	}


	override fun terminate() {
		val state = state
		if (state == State.closed)
			throw IOException("Cannot operate on a closed writer")

		close()

		withErrorChecking {
			if (state != State.end)
				throw JSONException("JSONWriter was not terminated with a complete JSON value")
		}
	}


	override fun writeBoolean(value: Boolean) {
		withErrorChecking {
			startValue(isString = false)

			destination.write(if (value) "true" else "false")
		}
	}


	override fun writeByte(value: Byte) {
		withErrorChecking {
			startValue(isString = false)

			destination.write(value.toString())
		}
	}


	override fun writeDouble(value: Double) {
		withErrorChecking {
			startValue(isString = false)

			if (!value.isFinite()) {
				throw JSONException("Cannot write double value '$value'")
			}

			destination.write(value.toString())
		}
	}


	override fun writeFloat(value: Float) {
		withErrorChecking {
			startValue(isString = false)

			if (!value.isFinite()) {
				throw JSONException("Cannot write float value '$value'")
			}

			destination.write(value.toString())
		}
	}


	override fun writeInt(value: Int) {
		withErrorChecking {
			startValue(isString = false)

			destination.write(value.toString())
		}
	}


	override fun writeLong(value: Long) {
		withErrorChecking {
			startValue(isString = false)

			destination.write(value.toString())
		}
	}


	override fun writeListEnd() {
		withErrorChecking {
			when (state) {
				State.afterListStart, State.afterListElement -> Unit
				State.closed -> throw IOException("Cannot operate on a closed writer")
				else -> throw JSONException("Cannot write end of list when not in a list")
			}

			destination.write(JSONCharacter.Symbol.rightSquareBracket)
			state = stateStack.removeAt(stateStack.size - 1)
		}
	}


	override fun writeListStart() {
		withErrorChecking {
			startValue(isString = false)

			destination.write(JSONCharacter.Symbol.leftSquareBracket)

			stateStack += state
			state = State.afterListStart
		}
	}


	override fun writeMapEnd() {
		withErrorChecking {
			when (state) {
				State.afterMapStart, State.afterMapElement -> Unit
				State.afterMapKey -> throw JSONException("Cannot write end of map right after a key, value expected instead")
				State.closed -> throw IOException("Cannot operate on a closed writer")
				else -> throw JSONException("Cannot write end of map when not in a map")
			}

			destination.write(JSONCharacter.Symbol.rightCurlyBracket)
			state = stateStack.removeAt(stateStack.size - 1)
		}
	}


	override fun writeMapStart() {
		withErrorChecking {
			startValue(isString = false)

			destination.write(JSONCharacter.Symbol.leftCurlyBracket)

			stateStack += state
			state = State.afterMapStart
		}
	}


	override fun writeNull() {
		withErrorChecking {
			startValue(isString = false)

			destination.write("null")
		}
	}


	override fun writeShort(value: Short) {
		withErrorChecking {
			startValue(isString = false)

			destination.write(value.toString())
		}
	}


	override fun writeString(value: String) {
		withErrorChecking {
			startValue(isString = true)

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


	private companion object {

		val hexCharacters = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
	}


	private enum class State {

		afterListElement,
		afterListStart,
		afterMapElement,
		afterMapKey,
		afterMapStart,
		closed,
		end,
		initial
	}
}
