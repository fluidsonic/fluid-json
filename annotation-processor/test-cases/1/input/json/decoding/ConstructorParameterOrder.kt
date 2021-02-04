package json.decoding

import io.fluidsonic.json.*


// check that finding the right constructor using reflection uses the correct order of parameters
@Json(
	encoding = Json.Encoding.none
)
class ConstructorParameterOrder(
	val b: String = "b",
	val a: String = "a",
	val c: String = "c"
)
