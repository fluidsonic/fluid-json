package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
class JSONException constructor(message: String, cause: Throwable? = null)
	: RuntimeException(message, cause) {

	companion object {

		// FIXME add keypath
		internal fun unexpectedCharacter(character: Int, expected: String, characterIndex: Int) =
			JSONException("(UTF-16 offset $characterIndex) unexpected ${Character.toString(character)}, expected $expected")


		// FIXME add keypath
		internal fun unexpectedToken(token: JSONToken?, expected: String, characterIndex: Int): JSONException {
			val tokenString = if (token != null) "'$token'" else "<end of input>"
			return JSONException("(UTF-16 offset $characterIndex) unexpected token $tokenString, expected $expected")
		}
	}
}
