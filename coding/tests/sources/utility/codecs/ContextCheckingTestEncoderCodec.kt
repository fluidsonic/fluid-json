package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestEncoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONEncoderCodec<String, Context> {

	override fun encode(value: String, encoder: JSONEncoder<Context>) {
		assert(encoder.context).toBe(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val encodableClass = String::class
}
