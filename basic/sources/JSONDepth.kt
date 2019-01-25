package com.github.fluidsonic.fluid.json


inline class JSONDepth(val value: Int) : Comparable<JSONDepth> {

	override operator fun compareTo(other: JSONDepth) =
		value.compareTo(other.value)
}
