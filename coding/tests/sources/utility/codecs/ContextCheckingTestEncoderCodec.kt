package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal class ContextCheckingTestEncoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONEncoderCodec<String, Context> {

	override fun JSONEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJSONCodec.run { encode(value) }
	}


	override val encodableClass = String::class
}
