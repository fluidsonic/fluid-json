package com.github.fluidsonic.fluid.json

import java.io.Closeable
import java.io.Reader
import java.util.Arrays


internal class TextInput(private val source: Reader) : Closeable by source {

	private var bufferEndIndex = 0
	private var bufferIsLocked = false
	private var bufferStartIndex = 0
	private var readCharacterCountNotInBufferAnymore = 0
	private var sourceIsAtEnd = false


	private fun alignBufferStartIfNeeded(): Boolean {
		// note that we keep a character before the read position in order to allow for seeking back one character

		if (bufferIsLocked) {
			return false
		}

		val bufferStartIndex = bufferStartIndex
		if (bufferStartIndex <= 1) {
			return true
		}

		val unreadCharacterCount = bufferEndIndex - bufferStartIndex
		if (unreadCharacterCount <= 0) {
			this.buffer[0] = this.buffer[bufferStartIndex - 1]
			this.bufferEndIndex = 1
			this.bufferStartIndex = 1
			this.readCharacterCountNotInBufferAnymore += bufferStartIndex - 1

			return true
		}

		if (bufferStartIndex >= windowSize + 1) {
			System.arraycopy(buffer, bufferStartIndex - 1, buffer, 0, unreadCharacterCount)

			this.bufferEndIndex = 1 + unreadCharacterCount
			this.bufferStartIndex = 1
			this.readCharacterCountNotInBufferAnymore += bufferStartIndex + 1

			return true
		}

		return false
	}


	var buffer = CharArray((2 * windowSize) + 1)
		private set


	val index
		get() = bufferStartIndex


	fun <ReturnValue> locked(body: () -> ReturnValue): ReturnValue {
		lockBuffer()

		try {
			return body()
		}
		finally {
			unlockBuffer()
		}
	}


	fun lockBuffer() {
		check(!bufferIsLocked) { "Input is already locked." }
		bufferIsLocked = true
	}


	fun peekCharacter() =
		if (tryPreloadCharacters(1) > 0)
			buffer[bufferStartIndex].toInt()
		else
			Character.end


	fun readCharacter(): Int {
		val character = if (tryPreloadCharacters(1) > 0)
			buffer[bufferStartIndex].toInt()
		else
			Character.end

		bufferStartIndex += 1

		return character
	}


	fun readCharacter(required: Int) =
		readCharacter(required) { Character.toString(required) }


	inline fun readCharacter(required: Int, expected: () -> String) =
		readCharacter(required = { it == required }, expected = expected)


	inline fun readCharacter(required: (character: Int) -> Boolean, expected: () -> String): Int {
		val character = readCharacter()
		if (!required(character)) {
			throw JSONException.unexpectedCharacter(
				character,
				expected = expected(),
				characterIndex = bufferStartIndex - 1
			)
		}

		return character
	}


	fun seekBackOneCharacter() {
		assert(bufferStartIndex > 0)

		bufferStartIndex -= 1
	}


	fun skipWhitespaceCharacters() {
		var bufferStartIndex = bufferStartIndex
		var bufferEndIndex = bufferEndIndex

		do {
			if (bufferStartIndex >= bufferEndIndex) {
				this.bufferStartIndex = bufferStartIndex

				if (tryPreloadCharacters() == 0) {
					this.bufferStartIndex = bufferEndIndex
					return
				}

				bufferStartIndex = this.bufferStartIndex
				bufferEndIndex = this.bufferEndIndex
			}

			val character = buffer[bufferStartIndex].toInt()
			bufferStartIndex += 1
		}
		while (Character.isWhitespace(character))

		this.bufferStartIndex = bufferStartIndex - 1
	}


	val sourceIndex
		get() = readCharacterCountNotInBufferAnymore + bufferStartIndex


	private fun tryPreloadCharacters(preloadCount: Int = windowSize): Int {
		assert(preloadCount in 0 .. windowSize)

		var bufferEndIndex = bufferEndIndex

		val unreadCharacterCount = bufferEndIndex - bufferStartIndex
		if (unreadCharacterCount >= preloadCount) {
			return preloadCount
		}

		if (sourceIsAtEnd) {
			return 0
		}

		var buffer = buffer
		var bufferSize = buffer.size

		if (bufferSize - bufferEndIndex < windowSize) {
			if (alignBufferStartIfNeeded()) {
				bufferEndIndex = this.bufferEndIndex
			}
			else {
				bufferSize += windowSize
				buffer = Arrays.copyOf(buffer, bufferSize)

				this.buffer = buffer
			}
		}

		val preloadedCount = source.read(buffer, bufferEndIndex, windowSize)
		if (preloadedCount <= 0) {
			check(preloadedCount != 0) { "Reader.read(…) must not return 0." }

			sourceIsAtEnd = true
			return 0
		}

		bufferEndIndex += preloadedCount
		this.bufferEndIndex = bufferEndIndex

		return preloadedCount
	}


	fun unlockBuffer() {
		check(bufferIsLocked) { "Input is not locked." }
		bufferIsLocked = false

		alignBufferStartIfNeeded()
	}


	private companion object {

		const val windowSize = 2048
	}
}
