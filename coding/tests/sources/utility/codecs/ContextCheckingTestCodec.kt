package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.StringJSONCodec
import com.github.fluidsonic.fluid.json.jsonCodingType


internal class ContextCheckingTestCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONCodec<String, Context> {

	override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<String>): String {
		assert(context).toBe(expectedContext)

		return StringJSONCodec.run { decode(valueType) }
	}


	override fun JSONEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJSONCodec.run { encode(value) }
	}


	override val decodableType = jsonCodingType<String>()
}
