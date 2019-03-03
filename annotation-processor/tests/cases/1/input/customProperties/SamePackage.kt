package customProperties

import com.github.fluidsonic.fluid.json.*


@JSON
class SamePackage(val value: String) {

	@JSON.CustomProperties
	internal fun JSONEncoder<JSONCodingContext>.writeCustomProperties1() = Unit
}


@JSON.CustomProperties
internal fun JSONEncoder<JSONCodingContext>.writeCustomProperties2(value: SamePackage) = Unit
