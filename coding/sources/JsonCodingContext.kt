package io.fluidsonic.json


interface JsonCodingContext {

	companion object {

		val empty = object : JsonCodingContext {}
	}
}
