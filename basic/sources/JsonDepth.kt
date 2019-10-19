package io.fluidsonic.json


inline class JsonDepth(val value: Int) : Comparable<JsonDepth> {

	override operator fun compareTo(other: JsonDepth) =
		value.compareTo(other.value)
}
