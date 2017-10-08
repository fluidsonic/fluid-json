package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal class JSONCodecRegistry<in Context : JSONCoderContext>
private constructor(
	private val decoderCodecs: Map<Class<*>, JSONDecoderCodec<*, Context>>,
	private val encoderCodecs: Map<Class<*>, JSONEncoderCodec<*, Context>>
) {

	@API(status = API.Status.EXPERIMENTAL)
	@Suppress("UNCHECKED_CAST")
	fun <Value : Any> decoderCodecForClass(`class`: Class<Value>) =
		decoderCodecs[`class`] as JSONDecoderCodec<Value, Context>?


	@API(status = API.Status.EXPERIMENTAL)
	@Suppress("UNCHECKED_CAST")
	fun <Value : Any> encoderCodecForClass(`class`: Class<Value>) =
		encoderCodecs[`class`] as JSONEncoderCodec<Value, Context>?


	@API(status = API.Status.EXPERIMENTAL)
	companion object {

		@API(status = API.Status.EXPERIMENTAL)
		inline fun <Context : JSONCoderContext> build(body: Builder<Context>.() -> Unit) =
			Builder<Context>()
				.apply(body)
				.build()
	}


	@API(status = API.Status.EXPERIMENTAL)
	class Builder<Context : JSONCoderContext> {

		private val decoderCodecs = mutableMapOf<Class<*>, JSONDecoderCodec<*, Context>>()
		private val encoderCodecs = mutableMapOf<Class<*>, JSONEncoderCodec<*, Context>>()


		@API(status = API.Status.EXPERIMENTAL)
		@JvmName("addTwoWay")
		inline fun <Codec, reified Value : Any> add(vararg codecs: Codec)
			where Codec : JSONDecoderCodec<Value, Context>, Codec : JSONEncoderCodec<Value, Context> {
			for (codec in codecs) {
				decoderCodecs[Value::class.java] = codec
				encoderCodecs[Value::class.java] = codec
			}
		}


		@API(status = API.Status.EXPERIMENTAL)
		inline fun <reified Value : Any> add(vararg codecs: JSONDecoderCodec<Value, Context>): Builder<Context> {
			for (codec in codecs) {
				decoderCodecs[Value::class.java] = codec
			}

			return this
		}


		@API(status = API.Status.EXPERIMENTAL)
		inline fun <reified Value : Any> add(vararg codecs: JSONEncoderCodec<Value, Context>): Builder<Context> {
			for (codec in codecs) {
				encoderCodecs[Value::class.java] = codec
			}

			return this
		}


		@API(status = API.Status.EXPERIMENTAL)
		fun add(vararg registries: JSONCodecRegistry<Context>): Builder<Context> {
			for (registry in registries) {
				decoderCodecs += registry.decoderCodecs
				encoderCodecs += registry.encoderCodecs
			}

			return this
		}


		@API(status = API.Status.EXPERIMENTAL)
		fun build() =
			JSONCodecRegistry(
				decoderCodecs = decoderCodecs,
				encoderCodecs = encoderCodecs
			)
	}
}
