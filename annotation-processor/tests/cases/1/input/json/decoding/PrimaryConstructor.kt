package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.primaryConstructor
)
class PrimaryConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
