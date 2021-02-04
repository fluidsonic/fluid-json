package io.fluidsonic.json


public abstract class JsonException(
	message: String,
	public val offset: Int = -1,
	public val path: JsonPath? = null,
	cause: Throwable? = null
) : RuntimeException(message, cause) {

	override val message: String
		get() = buildMessage(
			message = super.message ?: "",
			offset = offset,
			path = path
		)


	public companion object {

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


	public class Parsing(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		public companion object
	}


	public class Schema(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		public companion object
	}


	public class Serialization(
		message: String,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, path = path, cause = cause) {

		public companion object
	}


	public class Syntax(
		message: String,
		offset: Int = -1,
		path: JsonPath? = null,
		cause: Throwable? = null
	) : JsonException(message = message, offset = offset, path = path, cause = cause) {

		public companion object
	}
}
