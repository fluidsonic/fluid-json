package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import kotlin.reflect.KClass


internal open class DummyJSONDecoder : DummyJSONReader(), JSONDecoder<JSONCoderContext> {

	override val context: JSONCoderContext
		get() = error("")


	override fun <Value : Any> readDecodableOfClass(valueClass: KClass<out Value>): Value = error("")
}
