package io.fluidsonic.json

import kotlin.reflect.*


public interface JsonEncoderCodec<in Value : Any, in Context : JsonCodingContext> : JsonCodecProvider<Context> {

	public val encodableClass: KClass<in Value>


	public fun JsonEncoder<Context>.encode(value: Value)


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? =
		null


	@Suppress("UNCHECKED_CAST")
	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? =
		if (encodableClass.isAssignableOrBoxableTo(this.encodableClass))
			this as JsonEncoderCodec<ActualValue, Context>
		else
			null


	public companion object
}
