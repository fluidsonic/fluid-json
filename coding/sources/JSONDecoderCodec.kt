package com.github.fluidsonic.fluid.json

import kotlin.reflect.*


interface JSONDecoderCodec<Value : Any, in Context : JSONCodingContext> : JSONCodecProvider<Context> {

	val decodableType: JSONCodingType<Value>


	fun JSONDecoder<Context>.decode(valueType: JSONCodingType<Value>): Value


	@Suppress("UNCHECKED_CAST")
	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? =
		if (this.decodableType.satisfiesType(decodableType))
			this as JSONDecoderCodec<ActualValue, Context>
		else
			null


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? =
		null


	companion object {

		private fun JSONCodingType<*>.satisfiesType(requestedType: JSONCodingType<*>): Boolean {
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
