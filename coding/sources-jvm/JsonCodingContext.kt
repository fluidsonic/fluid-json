package io.fluidsonic.json


/**
 * Context passed to codecs during JSON encoding and decoding, allowing them to access application-specific state.
 */
public interface JsonCodingContext {

	public companion object {

		public val empty: JsonCodingContext = object : JsonCodingContext {}
	}
}
