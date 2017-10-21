package tests


internal fun <T> emptyEquatableSequence(): Sequence<T> =
	EquatableSequence(emptySequence())


internal fun <T> equatableSequenceOf(vararg elements: T): Sequence<T> =
	EquatableSequence(elements.asSequence())


@Suppress("EqualsOrHashCode")
internal class EquatableSequence<out Element>(val source: Sequence<Element>) : Sequence<Element> by source {

	// careful, this isn't reflexive
	override fun equals(other: Any?): Boolean {
		if (other === this) {
			return true
		}
		if (other !is Sequence<*>) {
			return false
		}

		return toList() == other.toList()
	}
}
