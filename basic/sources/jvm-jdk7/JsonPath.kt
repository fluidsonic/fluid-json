package io.fluidsonic.json


public class JsonPath(elements: List<Element> = emptyList()) {

	public val elements: List<Element> = elements.toList()

	override fun equals(other: Any?): Boolean = (this === other || (other is JsonPath && elements == other.elements))
	override fun hashCode(): Int = elements.hashCode()

	override fun toString(): String =
		if (elements.isNotEmpty())
			elements.joinToString(separator = "", prefix = "<root>")
		else
			"<root>"


	public companion object {

		public val root: JsonPath = JsonPath()
	}


	public sealed class Element {

		public class ListIndex(public val value: Int) : Element() {

			override fun equals(other: Any?): Boolean = (this === other || (other is ListIndex && value == other.value))
			override fun hashCode(): Int = value
			override fun toString(): String = "[$value]"
		}


		public class MapKey(public val value: String) : Element() {

			override fun equals(other: Any?): Boolean = (this === other || (other is MapKey && value == other.value))
			override fun hashCode(): Int = value.hashCode()
			override fun toString(): String = ".\"$value\""
		}
	}
}
