package com.github.fluidsonic.fluid.json


abstract class JSONException(
	message: String,
	val offset: Int = -1,
	val path: JSONPath? = null,
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
			path: JSONPath?
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
		path: JSONPath? = null,
		cause: Throwable? = null
	) : JSONException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}


	class Schema(
		message: String,
		offset: Int = -1,
		path: JSONPath? = null,
		cause: Throwable? = null
	) : JSONException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}


	class Serialization(
		message: String,
		path: JSONPath? = null,
		cause: Throwable? = null
	) : JSONException(message = message, path = path, cause = cause) {

		companion object
	}


	class Syntax(
		message: String,
		offset: Int = -1,
		path: JSONPath? = null,
		cause: Throwable? = null
	) : JSONException(message = message, offset = offset, path = path, cause = cause) {

		companion object
	}
}
