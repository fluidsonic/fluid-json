package customProperties.differentPackage

import io.fluidsonic.json.*
import customProperties.DifferentPackage


@Json.CustomProperties
internal fun JsonEncoder<JsonCodingContext>.writeCustomProperties3(value: DifferentPackage) = Unit
