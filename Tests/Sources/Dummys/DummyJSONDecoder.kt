package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder


internal open class DummyJSONDecoder : DummyJSONReader(), JSONDecoder<JSONCoderContext> {

	override val context: JSONCoderContext
		get() = error("")


	override fun <Value : Any> readDecodableOfClass(valueClass: Class<out Value>): Value = error("")
}
