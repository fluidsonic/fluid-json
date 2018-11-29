package com.github.fluidsonic.fluid.json


interface JSONCodingSerializer : JSONSerializer {

	override fun serializeValue(value: Any?, destination: JSONWriter)


	companion object {

		fun builder(): BuilderForEncoding<JSONCodingContext> =
			BuilderForEncodingImpl(context = JSONCodingContext.empty)


		fun <Context : JSONCodingContext> builder(context: Context): BuilderForEncoding<Context> =
			BuilderForEncodingImpl(context = context)


		val default = builder()
			.encodingWith()
			.build()


		val nonRecursive = builder()
			.encodingWith(DefaultJSONCodecs.nonRecursive)
			.build()


		interface BuilderForEncoding<Context : JSONCodingContext> {

			fun encodingWith(factory: (destination: JSONWriter, context: Context) -> JSONEncoder<Context>): Builder


			fun encodingWith(
				vararg providers: JSONCodecProvider<Context>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				encodingWith(providers = providers.toList(), base = base)


			fun encodingWith(
				providers: Iterable<JSONCodecProvider<Context>>,
				base: JSONCodecProvider<JSONCodingContext>? = JSONCodecProvider.extended
			) =
				encodingWith { destination, context ->
					JSONEncoder.builder(context)
						.codecs(JSONCodecProvider.of(providers = providers, base = base))
						.destination(destination)
						.build()
				}
		}


		private class BuilderForEncodingImpl<Context : JSONCodingContext>(
			private val context: Context
		) : BuilderForEncoding<Context> {

			override fun encodingWith(factory: (source: JSONWriter, context: Context) -> JSONEncoder<Context>) =
				BuilderImpl(
					context = context,
					encoderFactory = factory
				)
		}


		interface Builder {

			fun build(): JSONCodingSerializer
		}


		private class BuilderImpl<out Context : JSONCodingContext>(
			private val context: Context,
			private val encoderFactory: (source: JSONWriter, context: Context) -> JSONEncoder<Context>
		) : Builder {

			override fun build() =
				StandardCodingSerializer(
					context = context,
					encoderFactory = encoderFactory
				)
		}
	}
}
