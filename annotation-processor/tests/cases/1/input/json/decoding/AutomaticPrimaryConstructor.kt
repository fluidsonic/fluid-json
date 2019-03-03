package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.automatic
)
class AutomaticPrimaryConstructor(
	val value: String
)
