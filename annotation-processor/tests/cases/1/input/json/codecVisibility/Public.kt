package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.CodecVisibility.publicRequired
)
class Public(val value: String)
