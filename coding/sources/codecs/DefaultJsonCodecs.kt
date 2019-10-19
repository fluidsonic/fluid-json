package io.fluidsonic.json

import io.fluidsonic.json.dynamic.*


object DefaultJsonCodecs {

	val basic: List<JsonCodecProvider<JsonCodingContext>> =
		listOf(
			AnyJsonDecoderCodec,
			ArrayJsonCodec,
			BooleanArrayJsonCodec,
			BooleanJsonCodec,
			ByteArrayJsonCodec,
			ByteJsonCodec,
			CharJsonCodec,
			DoubleArrayJsonCodec,
			DoubleJsonCodec,
			FloatArrayJsonCodec,
			FloatJsonCodec,
			IntArrayJsonCodec,
			IntJsonCodec,
			ListJsonDecoderCodec,
			LongArrayJsonCodec,
			LongJsonCodec,
			MapJsonCodec,
			SequenceJsonCodec,
			SetJsonDecoderCodec,
			ShortArrayJsonCodec,
			ShortJsonCodec,
			StringJsonCodec,
			CollectionJsonCodec, // after subclasses
			IterableJsonEncoderCodec, // after subclasses
			NumberJsonCodec // after subclasses
		)


	val extended: List<JsonCodecProvider<JsonCodingContext>> =
		codingImplementationsJava.extendedCodecProviders()


	val nonRecursive: List<JsonCodecProvider<JsonCodingContext>> =
		listOf(
			ArrayJsonCodec.nonRecursive,
			ListJsonDecoderCodec.nonRecursive,
			MapJsonCodec.nonRecursive,
			SequenceJsonCodec.nonRecursive,
			SetJsonDecoderCodec.NON_RECURSIVE,
			CollectionJsonCodec.nonRecursive, // after subclasses
			IterableJsonEncoderCodec.nonRecursive // after subclasses
		)
}
