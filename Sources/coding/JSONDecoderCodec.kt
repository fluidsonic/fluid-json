package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONDecoderCodec<Value : Any, in Context : JSONCodingContext> : JSONCodecProvider<Context> {

	val decodableType: JSONCodingType<Value>


	fun decode(valueType: JSONCodingType<in Value>, decoder: JSONDecoder<Context>): Value


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodingType<in Value>): JSONDecoderCodec<out Value, Context>? =
		if (this.decodableType.satisfiesType(decodableType))
			this as JSONDecoderCodec<out Value, Context>
		else
			null


	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? =
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
