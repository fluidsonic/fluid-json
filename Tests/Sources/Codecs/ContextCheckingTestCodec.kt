package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.StringJSONCodec
import com.winterbe.expekt.should


internal class ContextCheckingTestCodec<in Context : JSONCoderContext>(
	private val expectedContext: Context
) : JSONCodec<String, Context> {

	override fun decode(decoder: JSONDecoder<Context>): String {
		decoder.context.should.equal(expectedContext)

		return StringJSONCodec.decode(decoder)
	}


	override fun encode(value: String, encoder: JSONEncoder<Context>) {
		encoder.context.should.equal(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val valueClass = String::class.java
}
