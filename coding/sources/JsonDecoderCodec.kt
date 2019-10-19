package io.fluidsonic.json

import kotlin.reflect.*


interface JsonDecoderCodec<Value : Any, in Context : JsonCodingContext> : JsonCodecProvider<Context> {

	val decodableType: JsonCodingType<Value>


	fun JsonDecoder<Context>.decode(valueType: JsonCodingType<Value>): Value


	@Suppress("UNCHECKED_CAST")
	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? =
		if (this.decodableType.satisfiesType(decodableType))
			this as JsonDecoderCodec<ActualValue, Context>
		else
			null


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? =
		null


	companion object {

		private fun JsonCodingType<*>.satisfiesType(requestedType: JsonCodingType<*>): Boolean {
			if (isUnconstrainedUpperBoundInGenericContext) {
				return true
			}
			if (requestedType.rawClass != rawClass) {
				return false
			}

			val decodableArguments = arguments
			val requestedArguments = requestedType.arguments
			assert(decodableArguments.size == requestedArguments.size)

			@Suppress("LoopToCallChain")
			for ((index, decodableArgument) in decodableArguments.withIndex()) {
				if (!decodableArgument.satisfiesType(requestedArguments[index])) {
					return false
				}
			}

			return true
		}
	}
}
