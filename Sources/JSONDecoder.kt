package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal class JSONDecoder<Context : JSONCoderContext>(
	val codecs: JSONCodecRegistry<Context>,
	val context: Context,
	reader: JSONReader
) : JSONReader by reader {

	@API(status = API.Status.EXPERIMENTAL)
	inline fun <reified Value : Any> readDecodable() =
		readDecodableOrNull<Value>()
			?: throw JSONException("unexpected null value when reading value of ${Value::class.java}")


	@API(status = API.Status.EXPERIMENTAL)
	inline fun <reified Value : Any> readDecodableOrNull(): Value? {
		if (nextToken == JSONToken.nullValue) {
			return null
		}

		val codec = codecs.decoderCodecForClass(Value::class.java)
			?: throw JSONException("no decoder codec registered for ${Value::class.java}")

		return codec.decode(decoder = this)
	}
}
