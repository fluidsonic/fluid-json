package io.fluidsonic.json


interface JsonCodingSerializer : JsonSerializer {

	override fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean)


	companion object {

		fun builder(): BuilderForEncoding<JsonCodingContext> =
			BuilderForEncodingImpl(context = JsonCodingContext.empty)


		fun <Context : JsonCodingContext> builder(context: Context): BuilderForEncoding<Context> =
			BuilderForEncodingImpl(context = context)


		val default = builder()
			.encodingWith()
			.build()


		val nonRecursive = builder()
			.encodingWith(DefaultJsonCodecs.nonRecursive)
			.build()


		interface BuilderForEncoding<Context : JsonCodingContext> {

			fun encodingWith(factory: (destination: JsonWriter, context: Context) -> JsonEncoder<Context>): Builder


			fun encodingWith(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			) =
				encodingWith(providers = providers.toList(), base = base)


			fun encodingWith(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			) =
				encodingWith { destination, context ->
					JsonEncoder.builder(context)
						.codecs(JsonCodecProvider(providers), base = base)
						.destination(destination)
						.build()
				}
		}


		private class BuilderForEncodingImpl<Context : JsonCodingContext>(
			private val context: Context
		) : BuilderForEncoding<Context> {

			override fun encodingWith(factory: (source: JsonWriter, context: Context) -> JsonEncoder<Context>) =
				BuilderImpl(
					context = context,
					encoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JsonCodingSerializer
		}


		private class BuilderImpl<out Context : JsonCodingContext>(
			private val context: Context,
			private val encoderFactory: (source: JsonWriter, context: Context) -> JsonEncoder<Context>
		) : Builder {

			override fun build() =
				StandardCodingSerializer(
					context = context,
					encoderFactory = encoderFactory
				)
		}
	}
}
