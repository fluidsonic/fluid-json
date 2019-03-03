package json.classes

import com.github.fluidsonic.fluid.json.*


@JSON
data class GenericClass<A, B : GenericClass.Bound, C : Any>(
	val a: A,
	val b: B,
	val c: C
) {

	interface Bound
}
