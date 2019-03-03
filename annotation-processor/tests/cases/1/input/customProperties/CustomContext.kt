package customProperties

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.*


@JSON
class CustomContext(val value: String) {

	@JSON.CustomProperties
	internal fun JSONEncoder<CustomCodingContext>.writeCustomProperties1() = Unit
}


@JSON.CustomProperties
internal fun JSONEncoder<CustomCodingContext>.writeCustomProperties2(value: CustomContext) = Unit
