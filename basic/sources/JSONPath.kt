package com.github.fluidsonic.fluid.json


class JSONPath(elements: List<Element> = emptyList()) {

	val elements = elements.toList()

	override fun equals(other: Any?) = (this === other || (other is JSONPath && elements == other.elements))
	override fun hashCode() = elements.hashCode()

	override fun toString() =
		if (elements.isNotEmpty())
			elements.joinToString(separator = "", prefix = "<root>")
		else
			"<root>"


	companion object {

		val root = JSONPath()
	}


	sealed class Element {

		class ListIndex(val value: Int) : Element() {

			override fun equals(other: Any?) = (this === other || (other is ListIndex && value == other.value))
			override fun hashCode() = value
			override fun toString() = "[$value]"
		}


		class MapKey(val value: String) : Element() {

			override fun equals(other: Any?) = (this === other || (other is MapKey && value == other.value))
			override fun hashCode() = value.hashCode()
			override fun toString() = ".\"$value\""
		}
	}
}
