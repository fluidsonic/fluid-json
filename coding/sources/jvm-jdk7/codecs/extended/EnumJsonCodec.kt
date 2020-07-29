package io.fluidsonic.json

import kotlin.reflect.*


public sealed class EnumJsonCodec<Value : Enum<*>>(enumClass: KClass<Value>) : AbstractJsonCodec<Value, JsonCodingContext>(
	decodableType = jsonCodingType(enumClass)
) {

	internal class OrdinalBased<Value : Enum<*>>(
		enumClass: KClass<Value>
	) : EnumJsonCodec<Value>(enumClass = enumClass) {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Value>): Value {
			@Suppress("UNCHECKED_CAST")
			val values = valueType.rawClass.java.enumConstants as Array<out Value>

			return readInt().let { ordinal ->
				values.getOrElse(ordinal) {
					invalidValueError("expected an integer between 0 and ${values.size - 1}")
				}
			}
		}


		override fun JsonEncoder<JsonCodingContext>.encode(value: Value) {
			writeInt(value.ordinal)
		}
	}


	internal class StringBased<Value : Enum<*>>(
		enumClass: KClass<Value>,
		strings: List<String>
	) : EnumJsonCodec<Value>(enumClass = enumClass) {

		private val values: Array<Pair<Value, String>>


		init {
			values = enumClass.java.enumConstants
				.mapIndexed { index, value ->
					val string = strings[index]

					value to string
				}
				.toTypedArray()
		}


		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Value>) =
			readString().let { name ->
				values.firstOrNull { it.second == name }?.first
					?: invalidValueError("expected one of: ${values.joinToString { "\"${it.second}\"" }}")
			}


		override fun JsonEncoder<JsonCodingContext>.encode(value: Value) {
			writeString(values.first { it.first === value }.second)
		}
	}
}


@Suppress("FunctionName")
public inline fun <reified Value : Enum<Value>> EnumJsonCodec(
	transformation: EnumJsonTransformation
): EnumJsonCodec<Value> =
	EnumJsonCodec(
		enumClass = Value::class,
		transformation = transformation
	)


@Suppress("FunctionName")
public fun <Value : Enum<*>> EnumJsonCodec(
	enumClass: KClass<Value>,
	transformation: EnumJsonTransformation
): EnumJsonCodec<Value> =
	when (transformation) {
		is EnumJsonTransformation.Name ->
			EnumJsonCodec.StringBased(
				enumClass = enumClass,
				strings = enumClass.java.enumConstants.map { transformation.case.convert(it.name) }
			)

		is EnumJsonTransformation.Ordinal ->
			EnumJsonCodec.OrdinalBased(enumClass = enumClass)

		is EnumJsonTransformation.ToString ->
			EnumJsonCodec.StringBased(
				enumClass = enumClass,
				strings = enumClass.java.enumConstants.map { transformation.case.convert(it.toString()) }
			)
	}
