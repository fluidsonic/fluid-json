package json.codecVisibility

import com.github.fluidsonic.fluid.json.*


internal class AutomaticInternalForContainedClass {

	@JSON(
		codecVisibility = JSON.CodecVisibility.automatic
	)
	class ContainedClass(val value: String)
}
