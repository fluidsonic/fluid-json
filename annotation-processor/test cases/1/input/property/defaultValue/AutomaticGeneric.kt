package property.defaultValue

import io.fluidsonic.json.*


@Json
class AutomaticGeneric<T : AutomaticGeneric.SomeInterface>(
	val value1: T,
	val value2: T = error(""),
	val value3: T?,
	val value4: T? = error("")
) {

	interface SomeInterface
}
