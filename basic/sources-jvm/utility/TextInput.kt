package io.fluidsonic.json

import java.io.*
import kotlin.contracts.*


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
			System.arraycopy(buffer, bufferStartIndex - 1, buffer, 0, unreadCharacterCount + 1)

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


	fun lockBuffer() {
		check(!bufferIsLocked) { "Input is already locked." }
		bufferIsLocked = true
	}


	fun peekCharacter() =
		if (tryPreloadCharacters(1) > 0)
			buffer[bufferStartIndex].code
		else
			JsonCharacter.end


	fun readCharacter(): Int {
		val character = if (tryPreloadCharacters(1) > 0)
			buffer[bufferStartIndex].code
		else
			JsonCharacter.end

		bufferStartIndex += 1

		return character
	}


	fun seekBackOneCharacter() {
		check(bufferStartIndex > 0)

		bufferStartIndex -= 1
	}


	fun seekTo(index: Int) {
		check(bufferIsLocked) { "This operation is only possible with a locked buffer." }
		check(index in 0 until bufferEndIndex) { "index $index out of bounds 0 ..< $bufferEndIndex" }

		bufferStartIndex = index
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

			val character = buffer[bufferStartIndex].code
			bufferStartIndex += 1
		}
		while (JsonCharacter.isWhitespace(character))

		this.bufferStartIndex = bufferStartIndex - 1
	}


	val sourceIndex
		get() = readCharacterCountNotInBufferAnymore + bufferStartIndex


	private fun tryPreloadCharacters(preloadCount: Int = windowSize): Int {
		require(preloadCount in 0 .. windowSize)

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
				buffer = buffer.copyOf(bufferSize)

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


internal fun <ReturnValue> TextInput.locked(block: () -> ReturnValue): ReturnValue {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

	lockBuffer()

	try {
		return block()
	}
	finally {
		unlockBuffer()
	}
}
