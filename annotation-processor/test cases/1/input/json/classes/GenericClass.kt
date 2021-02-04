package json.classes

import io.fluidsonic.json.*


@Json
data class GenericClass<A, B : GenericClass.Bound, C : Any>(
	val a: A,
	val b: B,
	val c: C
) {

	interface Bound
}
