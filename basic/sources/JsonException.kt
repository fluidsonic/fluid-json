package io.fluidsonic.json


abstract class JsonException(
	message: String,
	val offset: Int = -1,
	val path: JsonPath? = null,
	cause: Throwable? = null
) : RuntimeException(message, cause) {

	override val message
		get() = buildMessage(
			message = super.message ?: "",
			offset = offset,
			path = path
		)


	companion object {

		private fun buildMessage(
			message: String,
			offset: Int,
			path: JsonPath?
		) = buildString {
			if (path != null) {
				append("at ")
				append(path.toString())
				append(": ")
			}

			append(message)

			if (offset >= 0) {
				append(" - at position ")
				append(offset)
			}
		}
	}


	class Parsing(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}


	class Schema(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}


	class Serialization(
		message: String,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, path = path, cause = cause) {

		companion object
	}


	class Syntax(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}
}
