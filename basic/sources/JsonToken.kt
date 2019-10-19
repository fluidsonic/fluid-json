package io.fluidsonic.json


enum class JsonToken {

	booleanValue,
	listEnd,
	listStart,
	nullValue,
	mapEnd,
	mapKey,
	mapStart,
	numberValue,
	stringValue;


	override fun toString() =
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


	companion object
}


fun JsonToken?.toString() =
	this?.toString() ?: "end of data"
