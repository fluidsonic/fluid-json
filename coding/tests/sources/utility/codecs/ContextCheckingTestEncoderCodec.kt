package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*


internal class ContextCheckingTestEncoderCodec<in Context : JsonCodingContext>(
	private val expectedContext: Context
) : JsonEncoderCodec<String, Context> {

	override fun JsonEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJsonCodec.run { encode(value) }
	}


	override val encodableClass = String::class
}
