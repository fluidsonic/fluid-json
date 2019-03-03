package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.automatic
)
class AutomaticAnnotatedConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())

	@JSON.Constructor
	constructor(value: Long) : this(value.toString())
}
