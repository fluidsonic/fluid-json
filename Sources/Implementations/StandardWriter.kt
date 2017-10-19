package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Flushable
import java.io.IOException
import java.io.Writer


internal class StandardWriter(private val destination: Writer)
	: JSONWriter, Closeable, Flushable by destination {

	private var state = State.initial
	private val stateStack = mutableListOf<State>()


	override fun close() {
		val stateBeforeClosing = state
		if (stateBeforeClosing == State.closed) {
			return
		}

		state = State.closed

		destination.close()

		if (stateBeforeClosing != State.end) {
			throw JSONException("Cannot close writer before all values were fully written.")
		}
	}


	private fun startValue(isString: Boolean) {
		when (state) {
			State.afterListElement ->
				destination.write(Character.Symbol.comma)

			State.afterListStart ->
				state = State.afterListElement

			State.afterMapElement ->
				if (isString) {
					destination.write(Character.Symbol.comma)
					state = State.afterMapKey
				}
				else {
					throw JSONException("Expected a string as map key")
				}

			State.afterMapKey -> {
				destination.write(Character.Symbol.colon)
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


	override fun writeBoolean(value: Boolean) {
		startValue(isString = false)

		destination.write(if (value) "true" else "false")
	}


	override fun writeByte(value: Byte) {
		startValue(isString = false)

		destination.write(value.toString())
	}


	override fun writeDouble(value: Double) {
		startValue(isString = false)

		if (!value.isFinite()) {
			throw JSONException("Cannot write double value '$value'")
		}

		destination.write(value.toString())
	}


	override fun writeFloat(value: Float) {
		startValue(isString = false)

		if (!value.isFinite()) {
			throw JSONException("Cannot write float value '$value'")
		}

		destination.write(value.toString())
	}


	override fun writeInt(value: Int) {
		startValue(isString = false)

		destination.write(value.toString())
	}


	override fun writeLong(value: Long) {
		startValue(isString = false)

		destination.write(value.toString())
	}


	override fun writeListEnd() {
		when (state) {
			State.afterListStart, State.afterListElement -> Unit
			State.closed -> throw IOException("Cannot operate on a closed writer")
			else -> throw JSONException("Cannot write end of list when not in a list")
		}

		destination.write(Character.Symbol.rightSquareBracket)
		state = stateStack.removeAt(stateStack.size - 1)
	}


	override fun writeListStart() {
		startValue(isString = false)

		destination.write(Character.Symbol.leftSquareBracket)

		stateStack += state
		state = State.afterListStart
	}


	override fun writeMapEnd() {
		when (state) {
			State.afterMapStart, State.afterMapElement -> Unit
			State.afterMapKey -> throw JSONException("Cannot write end of map right after a key, value expected instead")
			State.closed -> throw IOException("Cannot operate on a closed writer")
			else -> throw JSONException("Cannot write end of map when not in a map")
		}

		destination.write(Character.Symbol.rightCurlyBracket)
		state = stateStack.removeAt(stateStack.size - 1)
	}


	override fun writeMapStart() {
		startValue(isString = false)

		destination.write(Character.Symbol.leftCurlyBracket)

		stateStack += state
		state = State.afterMapStart
	}


	override fun writeNull() {
		startValue(isString = false)

		destination.write("null")
	}


	override fun writeShort(value: Short) {
		startValue(isString = false)

		destination.write(value.toString())
	}


	override fun writeString(value: String) {
		startValue(isString = true)

		// TODO optimize
		destination.write(Character.Symbol.quotationMark)

		for (character in value) {
			when (character) {
				'"', '\\' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(character.toInt())
				}

				'\b' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.b)
				}

				'\u000C' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.f)
				}

				'\n' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.n)
				}

				'\r' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.r)
				}

				'\t' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.t)
				}

				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014',
				'\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B', '\u001C',
				'\u001D', '\u001E', '\u001F' -> {
					destination.write(Character.Symbol.reverseSolidus)
					destination.write(Character.Letter.u)
					destination.write(Character.Digit.zero)
					destination.write(Character.Digit.zero)

					if (character.toInt() >= 0x10) {
						destination.write(Character.Digit.one)
					}
					else {
						destination.write(Character.Digit.zero)
					}

					destination.write(hexCharacters[character.toInt() and 0xF].toInt())
				}

				else ->
					destination.write(character.toInt())
			}
		}

		destination.write(Character.Symbol.quotationMark)
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
