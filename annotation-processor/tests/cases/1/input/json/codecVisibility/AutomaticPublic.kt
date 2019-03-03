package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.Visibility.automatic
)
class AutomaticPublic(val value: String)
