package tests

import com.github.fluidsonic.fluid.json.JSON
import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.SimpleEncoder
import org.jetbrains.spek.subject.SubjectSpek
import java.io.StringWriter


internal object JSONEncodingSpec : SubjectSpek<JSONEncoder<TestCoderContext>>({

	subject {
		SimpleEncoder(
			codecResolver = JSONCodecResolver.of(
				JaegerCodec,
				JaegerCodec.StatusCodec,
				KaijuCodec,
				KaijuCodec.StatusCodec,
				LocalDateCodec,
				UniverseCodec
			),
			context = TestCoderContext(),
			destination = JSON.writer(StringWriter())
		)
	}
})
