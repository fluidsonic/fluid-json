package com.github.fluidsonic.fluid.json

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


internal class FactoryCodecProvider<out Value : Any, in Context : JSONCodingContext> internal constructor(
	private val valueClass: KClass<Value>,
	private val factory: (actualClass: KClass<out Value>) -> JSONCodecProvider<Context>?
) : JSONCodecProvider<Context> {

	private val codecsByClass = ConcurrentHashMap<KClass<*>, Codecs<*, Context>>()


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JSONCodingType<ActualValue>): JSONDecoderCodec<ActualValue, Context>? =
		decodableType.rawClass.takeIfSubclassOf(valueClass)
			?.let { codecsForClass(it).decoderCodec }


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JSONEncoderCodec<ActualValue, Context>? =
		encodableClass.takeIfSubclassOf(valueClass)
			?.let { codecsForClass(it).encoderCodec }


	@Suppress("UNCHECKED_CAST")
	private fun <ActualValue : Any> codecsForClass(actualClass: KClass<ActualValue>) =
		codecsByClass.getOrPut(actualClass) {
			factory(actualClass as KClass<Value>)
				?.let { provider ->
					Codecs(
						decoderCodec = provider.decoderCodecForType(jsonCodingType(actualClass)),
						encoderCodec = provider.encoderCodecForClass(actualClass)
					)
				}
				?: Codecs.empty()
		} as Codecs<ActualValue, Context>


	private data class Codecs<Value : Any, in Context : JSONCodingContext>(
		val decoderCodec: JSONDecoderCodec<Value, Context>?,
		val encoderCodec: JSONEncoderCodec<Value, Context>?
	) {

		companion object {

			private val empty: Codecs<Any, JSONCodingContext> = Codecs(decoderCodec = null, encoderCodec = null)


			@Suppress("UNCHECKED_CAST")
			fun <Value : Any> empty() =
				empty as Codecs<Value, JSONCodingContext>
		}
	}
}


@JvmName("JSONCodecProviderForDecoding")
@Suppress("FunctionName")
fun JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONDecoderCodec<Any, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForDecodingWithContext")
@Suppress("FunctionName")
fun <Context : JSONCodingContext> JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONDecoderCodec<Any, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForEncoding")
@Suppress("FunctionName")
fun JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONEncoderCodec<Any, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForEncodingWithContext")
@Suppress("FunctionName")
fun <Context : JSONCodingContext> JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONEncoderCodec<Any, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForCoding")
@Suppress("FunctionName")
fun JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONCodec<Any, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForCodingWithContext")
@Suppress("FunctionName")
fun <Context : JSONCodingContext> JSONCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JSONCodec<Any, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JSONCodecProviderForDecodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONDecoderCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForDecodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONDecoderCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JSONCodecProviderForDecodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONDecoderCodec<Value, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForDecodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONDecoderCodec<Value, Context>?
): JSONCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JSONCodecProviderForEncodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONEncoderCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForEncodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONEncoderCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JSONCodecProviderForEncodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONEncoderCodec<Value, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForEncodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONEncoderCodec<Value, Context>?
): JSONCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JSONCodecProviderForCodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForCodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONCodec<Value, JSONCodingContext>?
): JSONCodecProvider<JSONCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JSONCodecProviderForCodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JSONCodec<Value, Context>?
): JSONCodecProvider<Context> =
	JSONCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JSONCodecProviderForCodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JSONCodingContext> JSONCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JSONCodec<Value, Context>?
): JSONCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)
