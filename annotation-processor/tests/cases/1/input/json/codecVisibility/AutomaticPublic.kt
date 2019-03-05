package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.CodecVisibility.automatic
)
class AutomaticPublic(val value: String)
