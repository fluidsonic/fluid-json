package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoder


internal open class DummyJSONEncoder : DummyJSONWriter(), JSONEncoder<JSONCoderContext> {

	override val context: JSONCoderContext
		get() = error("")


	override fun writeEncodable(value: Any): Unit = error("")
}
