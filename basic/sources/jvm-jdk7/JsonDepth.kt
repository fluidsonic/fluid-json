package io.fluidsonic.json


public inline class JsonDepth(public val value: Int) : Comparable<JsonDepth> {

	override operator fun compareTo(other: JsonDepth): Int =
		value.compareTo(other.value)
}
