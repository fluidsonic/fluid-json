package tests

import com.github.fluidsonic.fluid.json.JSONCodecRegistry
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONToken
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
			writer = object : JSONWriter {
				override val previousToken: JSONToken?
					get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

				override fun writeBoolean(value: Boolean) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeBooleanOrNull(value: Boolean?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeDouble(value: Double) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeDoubleOrNull(value: Double?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeFloat(value: Float) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeFloatOrNull(value: Float?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeInt(value: Int) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeIntOrNull(value: Int?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeListEnd() {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeListStart() {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeLong(value: Long) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeLongOrNUll(value: Long?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeMapEnd() {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeMapStart() {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeNull() {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeString(value: String) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

				override fun writeStringOrNull(value: String?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

			}
		)
	}
})
