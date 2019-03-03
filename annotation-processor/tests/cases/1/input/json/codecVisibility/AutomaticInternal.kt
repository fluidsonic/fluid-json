package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.Visibility.automatic
)
internal class AutomaticInternal(val value: String)
