package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.util.Arrays


internal class TextInput(private val source: Reader) {

	private var buffer = CharArray(2 * windowSize)
	private var bufferFill = 0
	private var bufferIndex = 0
	private var bufferLockCount = 0
	private var clearedBufferFill = 0
	private var sourceIsAtEnd = false


	val index
		get() = clearedBufferFill + bufferIndex


	fun lockBuffer() {
		check(bufferLockCount == 0) //FIXME
		bufferLockCount += 1
	}


	fun peekCharacter() =
		if (tryPreloadCharacters(1) > 0)
			buffer[bufferIndex].toInt()
		else
			Character.end


	fun readCharacter(): Int {
		val character = if (tryPreloadCharacters(1) > 0)
			buffer[bufferIndex].toInt()
		else
			Character.end

		bufferIndex += 1

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
				characterIndex = bufferIndex - 1
			)
		}

		return character
	}


	fun seekBackOneCharacter() {
		assert(bufferIndex > 0)

		bufferIndex -= 1
	}


	fun skipWhitespaceCharacters() {
		var bufferIndex = bufferIndex
		var bufferFill = bufferFill

		do {
			if (bufferIndex >= bufferFill) {
				if (tryPreloadCharacters() == 0) {
					this.bufferIndex = bufferFill
					return
				}

				bufferIndex = this.bufferIndex
				bufferFill = this.bufferFill
			}

			val character = buffer[bufferIndex++].toInt()
		}
		while (Character.isWhitespace(character))

		this.bufferIndex = bufferIndex - 1
	}


	private fun tryPreloadCharacters(preloadCount: Int = windowSize): Int {
		assert(preloadCount in 0 .. windowSize)

		var bufferFill = bufferFill
		val remainingCount = bufferFill - bufferIndex
		if (remainingCount >= preloadCount) {
			return preloadCount
		}

		if (sourceIsAtEnd) {
			return 0
		}

		var buffer = buffer
		var bufferSize = buffer.size

		if (bufferSize - bufferFill < windowSize) {
			var bufferIndex = bufferIndex
			if (bufferLockCount > 0 && bufferIndex >= windowSize) { // FIXME what if locked buffer is empty?
				bufferFill -= bufferIndex

				System.arraycopy(buffer, bufferIndex, buffer, 0, bufferFill)

				this.clearedBufferFill += bufferIndex
				this.bufferFill = bufferFill

				bufferIndex = 0
				this.bufferIndex = bufferIndex
			}
			else if (bufferLockCount == 0 && bufferIndex == bufferFill) {
				this.clearedBufferFill += bufferIndex

				bufferIndex = 0
				bufferFill = 0

				this.bufferIndex = bufferIndex
				this.bufferFill = bufferFill
			}
			else {
				bufferSize += windowSize
				buffer = Arrays.copyOf(buffer, bufferSize)

				this.buffer = buffer
			}
		}

		val preloadedCount = source.read(buffer, bufferFill, windowSize)
		if (preloadedCount <= 0) {
			check(preloadedCount != 0) { "Reader.read(…) must not return 0." }

			sourceIsAtEnd = true
			return 0
		}

		bufferFill += preloadedCount
		this.bufferFill = bufferFill

		return preloadedCount
	}


	fun unlockBuffer() {
		check(bufferLockCount > 0)

		if (bufferIndex > usedBufferClearThreshold) {
			val bufferIndex = bufferIndex
			val bufferFill = bufferFill - bufferIndex

			this.bufferFill = bufferFill
			this.bufferIndex = 0
			this.clearedBufferFill += bufferIndex

			System.arraycopy(buffer, bufferIndex, buffer, 0, bufferFill)
		}
	}


	private companion object {

		const val usedBufferClearThreshold = 512
		const val windowSize = 4096
	}
}
