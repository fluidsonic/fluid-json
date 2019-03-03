package customProperties.differentPackage

import com.github.fluidsonic.fluid.json.*
import customProperties.DifferentPackage


@JSON.CustomProperties
internal fun JSONEncoder<JSONCodingContext>.writeCustomProperties3(value: DifferentPackage) = Unit
