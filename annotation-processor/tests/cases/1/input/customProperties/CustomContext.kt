package customProperties

import codecProvider.CustomCodingContext
import io.fluidsonic.json.*


@Json
class CustomContext(val value: String) {

	@Json.CustomProperties
	internal fun JsonEncoder<CustomCodingContext>.writeCustomProperties1() = Unit
}


@Json.CustomProperties
internal fun JsonEncoder<CustomCodingContext>.writeCustomProperties2(value: CustomContext) = Unit
