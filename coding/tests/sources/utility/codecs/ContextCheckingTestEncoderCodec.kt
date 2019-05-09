package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestEncoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONEncoderCodec<String, Context> {

	override fun JSONEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJSONCodec.run { encode(value) }
	}


	override val encodableClass = String::class
}
