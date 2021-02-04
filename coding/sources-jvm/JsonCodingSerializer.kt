package io.fluidsonic.json


public interface JsonCodingSerializer : JsonSerializer {

	override fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean)


	public companion object {

		public fun builder(): BuilderForEncoding<JsonCodingContext> =
			BuilderForEncodingImpl(context = JsonCodingContext.empty)


		public fun <Context : JsonCodingContext> builder(context: Context): BuilderForEncoding<Context> =
			BuilderForEncodingImpl(context = context)


		public val default: JsonCodingSerializer = builder()
			.encodingWith()
			.build()


		public val nonRecursive: JsonCodingSerializer = builder()
			.encodingWith(DefaultJsonCodecs.nonRecursive)
			.build()


		public interface Builder {

			public fun build(): JsonCodingSerializer
		}


		public interface BuilderForEncoding<Context : JsonCodingContext> {

			public fun encodingWith(factory: (destination: JsonWriter, context: Context) -> JsonEncoder<Context>): Builder


			public fun encodingWith(
				vararg providers: JsonCodecProvider<Context>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): Builder =
				encodingWith(providers = providers.toList(), base = base)


			public fun encodingWith(
				providers: Iterable<JsonCodecProvider<Context>>,
				base: JsonCodecProvider<JsonCodingContext>? = JsonCodecProvider.extended
			): Builder =
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
