package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.Visibility.internal
)
class Internal(val value: String)
