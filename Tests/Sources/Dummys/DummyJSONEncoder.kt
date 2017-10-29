package tests

import com.github.fluidsonic.fluid.json.*


internal open class DummyJSONEncoder : DummyJSONWriter(), JSONEncoder<JSONCoderContext> {

	override val context: JSONCoderContext
		get() = error("")


	override fun writeValue(value: Any): Unit =
		error("")
}
