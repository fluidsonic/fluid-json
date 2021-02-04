package io.fluidsonic.json


public interface JsonCodingContext {

	public companion object {

		public val empty: JsonCodingContext = object : JsonCodingContext {}
	}
}
