package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.annotatedConstructor
)
class AnnotatedConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())

	@Json.Constructor
	constructor(value: Long) : this(value.toString())
}
