package io.fluidsonic.json


public enum class JsonToken {

	booleanValue,
	listEnd,
	listStart,
	nullValue,
	mapEnd,
	mapKey,
	mapStart,
	numberValue,
	stringValue;


	override fun toString(): String =
		when (this) {
			booleanValue -> "boolean"
			listEnd -> "list end"
			listStart -> "list"
			nullValue -> "null"
			mapEnd -> "map end"
			mapKey -> "map key"
			mapStart -> "map"
			numberValue -> "number"
			stringValue -> "string"
		}


	public companion object
}


public fun JsonToken?.toString(): String =
	this?.toString() ?: "end of data"
