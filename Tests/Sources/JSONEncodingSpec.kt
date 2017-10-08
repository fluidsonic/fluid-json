package tests

import com.github.fluidsonic.fluid.json.JSONCodecRegistry
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONWriter
import org.jetbrains.spek.subject.SubjectSpek


internal object JSONEncodingSpec : SubjectSpek<JSONEncoder<TestCoderContext>>({

	subject {
		JSONEncoder(
			codecs = JSONCodecRegistry.build {
				add(
					JaegerCodec,
					JaegerCodec.StatusCodec,
					KaijuCodec,
					KaijuCodec.StatusCodec,
					LocalDateCodec
				)
			},
			context = TestCoderContext(),
			writer = object : JSONWriter {} // next: implement JSONWriter & JSONReader
		)
	}
})
