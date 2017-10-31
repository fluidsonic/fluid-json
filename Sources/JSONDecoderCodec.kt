package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


interface JSONDecoderCodec<Value : Any, in Context : JSONCoderContext> : JSONCodecProvider<Context> {

	val decodableType: JSONCodableType<Value>


	fun decode(valueType: JSONCodableType<in Value>, decoder: JSONDecoder<out Context>): Value


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> decoderCodecForType(decodableType: JSONCodableType<in Value>): JSONDecoderCodec<out Value, Context>? =
		if (this.decodableType.satisfiesType(decodableType))
			this as JSONDecoderCodec<out Value, Context>
		else
			null


	override fun <Value : Any> encoderCodecForClass(encodableClass: KClass<out Value>): JSONEncoderCodec<in Value, Context>? =
		null


	companion object {

		private fun JSONCodableType<*>.satisfiesType(requestedType: JSONCodableType<*>): Boolean {
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
