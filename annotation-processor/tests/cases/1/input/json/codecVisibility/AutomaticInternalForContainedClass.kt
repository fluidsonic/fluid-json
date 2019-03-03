package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


internal class AutomaticInternalForContainedClass {

	@JSON(
		codecVisibility = JSON.Visibility.automatic
	)
	class ContainedClass(val value: String)
}
