package com.github.fluidsonic.fluid.json


class JSONException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {

	companion object {

		internal fun unexpectedCharacter(character: Int, expected: String, characterIndex: Int) =
			JSONException("(UTF-16 offset $characterIndex) unexpected ${JSONCharacter.toString(character)}, expected $expected")


		internal fun unexpectedToken(token: JSONToken?, expected: String, characterIndex: Int): JSONException {
			val tokenString = if (token != null) "'$token'" else "<end of input>"
			return JSONException("(UTF-16 offset $characterIndex) unexpected token $tokenString, expected $expected")
		}
	}
}
