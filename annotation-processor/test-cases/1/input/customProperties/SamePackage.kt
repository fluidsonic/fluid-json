package customProperties

import io.fluidsonic.json.*


@Json
class SamePackage(val value: String) {

	@Json.CustomProperties
	internal fun JsonEncoder<JsonCodingContext>.writeCustomProperties1() = Unit
}


@Json.CustomProperties
internal fun JsonEncoder<JsonCodingContext>.writeCustomProperties2(value: SamePackage) = Unit
