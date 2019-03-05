package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.CodecVisibility.internal
)
class Internal(val value: String)
