package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec
import com.winterbe.expekt.should


internal class ContextCheckingTestDecoderCodec<in Context : JSONCoderContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun decode(decoder: JSONDecoder<out Context>): String {
		decoder.context.should.equal(expectedContext)

		return StringJSONCodec.decode(decoder)
	}


	override val decodableClass = String::class.java
}
