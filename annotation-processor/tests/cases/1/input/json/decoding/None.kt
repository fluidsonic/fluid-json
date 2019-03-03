package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.none
)
class None(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
