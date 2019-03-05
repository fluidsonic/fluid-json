package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object CollectionJSONTestCodec : AbstractJSONCodec<Collection<*>, JSONCodingContext>(
	additionalProviders = listOf(StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Collection<*>>) =
		CollectionJSONCodec.run { decode(valueType) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Collection<*>) =
		CollectionJSONCodec.run { encode(value) }


	object NonRecursive : AbstractJSONCodec<Collection<*>, JSONCodingContext>(
		additionalProviders = listOf(StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Collection<*>>) =
			CollectionJSONCodec.nonRecursive.run { decode(valueType) }


		override fun JSONEncoder<JSONCodingContext>.encode(value: Collection<*>) =
			CollectionJSONCodec.nonRecursive.run { encode(value) }
	}
}
