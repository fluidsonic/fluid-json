package io.fluidsonic.json

import java.util.concurrent.*
import kotlin.reflect.*


internal class FactoryCodecProvider<out Value : Any, in Context : JsonCodingContext> internal constructor(
	private val valueClass: KClass<Value>,
	private val factory: (actualClass: KClass<out Value>) -> JsonCodecProvider<Context>?
) : JsonCodecProvider<Context> {

	private val codecsByClass = ConcurrentHashMap<KClass<*>, Codecs<*, Context>>()


	override fun <ActualValue : Any> decoderCodecForType(decodableType: JsonCodingType<ActualValue>): JsonDecoderCodec<ActualValue, Context>? =
		decodableType.rawClass.takeIfSubclassOf(valueClass)
			?.let { codecsForClass(it).decoderCodec }


	override fun <ActualValue : Any> encoderCodecForClass(encodableClass: KClass<ActualValue>): JsonEncoderCodec<ActualValue, Context>? =
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


	private data class Codecs<Value : Any, in Context : JsonCodingContext>(
		val decoderCodec: JsonDecoderCodec<Value, Context>?,
		val encoderCodec: JsonEncoderCodec<Value, Context>?
	) {

		companion object {

			private val empty: Codecs<Any, JsonCodingContext> = Codecs(decoderCodec = null, encoderCodec = null)


			@Suppress("UNCHECKED_CAST")
			fun <Value : Any> empty() =
				empty as Codecs<Value, JsonCodingContext>
		}
	}
}


@JvmName("JsonCodecProviderForDecoding")
@Suppress("FunctionName")
fun JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonDecoderCodec<Any, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForDecodingWithContext")
@Suppress("FunctionName")
fun <Context : JsonCodingContext> JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonDecoderCodec<Any, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForEncoding")
@Suppress("FunctionName")
fun JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonEncoderCodec<Any, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForEncodingWithContext")
@Suppress("FunctionName")
fun <Context : JsonCodingContext> JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonEncoderCodec<Any, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForCoding")
@Suppress("FunctionName")
fun JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonCodec<Any, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForCodingWithContext")
@Suppress("FunctionName")
fun <Context : JsonCodingContext> JsonCodecProvider.Companion.factory(
	factory: (valueClass: KClass<out Any>) -> JsonCodec<Any, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Any::class, factory = factory)


@JvmName("JsonCodecProviderForDecodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonDecoderCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForDecodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonDecoderCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JsonCodecProviderForDecodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonDecoderCodec<Value, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForDecodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonDecoderCodec<Value, Context>?
): JsonCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JsonCodecProviderForEncodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonEncoderCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForEncodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonEncoderCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JsonCodecProviderForEncodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonEncoderCodec<Value, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForEncodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonEncoderCodec<Value, Context>?
): JsonCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JsonCodecProviderForCodingSpecificValue")
@Suppress("FunctionName")
inline fun <reified Value : Any> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForCodingSpecificValue")
@Suppress("FunctionName")
fun <Value : Any> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonCodec<Value, JsonCodingContext>?
): JsonCodecProvider<JsonCodingContext> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)


@JvmName("JsonCodecProviderForCodingSpecificValueWithContext")
@Suppress("FunctionName")
inline fun <reified Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	noinline factory: (valueClass: KClass<out Value>) -> JsonCodec<Value, Context>?
): JsonCodecProvider<Context> =
	JsonCodecProvider.factoryOf(valueClass = Value::class, factory = factory)


@JvmName("JsonCodecProviderForCodingSpecificValueWithContext")
@Suppress("FunctionName")
fun <Value : Any, Context : JsonCodingContext> JsonCodecProvider.Companion.factoryOf(
	valueClass: KClass<out Value>,
	factory: (valueClass: KClass<out Value>) -> JsonCodec<Value, Context>?
): JsonCodecProvider<Context> =
	FactoryCodecProvider(valueClass = valueClass, factory = factory)
