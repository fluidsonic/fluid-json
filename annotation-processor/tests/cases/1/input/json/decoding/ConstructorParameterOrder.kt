package json.decoding

import com.github.fluidsonic.fluid.json.*


// check that finding the right constructor using reflection uses the correct order of parameters
@JSON(
	encoding = JSON.Encoding.none
)
class ConstructorParameterOrder(
	val b: String = "b",
	val a: String = "a",
	val c: String = "c"
)
