package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec
import com.github.fluidsonic.fluid.json.jsonCodingType


internal class ContextCheckingTestDecoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<in String>): String {
		assert(context).toBe(expectedContext)

		return StringJSONCodec.run { decode(valueType) }
	}


	override val decodableType = jsonCodingType<String>()
}
