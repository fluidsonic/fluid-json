package io.fluidsonic.json


/** Represents the nesting depth within a JSON structure. */
@JvmInline
public value class JsonDepth(public val value: Int) : Comparable<JsonDepth> {

	override operator fun compareTo(other: JsonDepth): Int =
		value.compareTo(other.value)
}
