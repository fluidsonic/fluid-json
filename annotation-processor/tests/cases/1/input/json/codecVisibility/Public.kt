package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecVisibility = JSON.Visibility.publicRequired
)
class Public(val value: String)
