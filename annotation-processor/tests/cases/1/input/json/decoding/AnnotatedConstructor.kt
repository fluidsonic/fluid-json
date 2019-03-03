package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.annotatedConstructor
)
class AnnotatedConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())

	@JSON.Constructor
	constructor(value: Long) : this(value.toString())
}
