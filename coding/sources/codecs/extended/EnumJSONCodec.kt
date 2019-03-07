package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


sealed class EnumJSONCodec<Value : Enum<*>>(enumClass: KClass<Value>) : AbstractJSONCodec<Value, JSONCodingContext>(
	decodableType = jsonCodingType(enumClass)
) {

	internal class OrdinalBased<Value : Enum<*>>(
		enumClass: KClass<Value>
	) : EnumJSONCodec<Value>(enumClass = enumClass) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Value>): Value {
			@Suppress("UNCHECKED_CAST")
			val values = valueType.rawClass.java.enumConstants as Array<out Value>

			return readInt().let { ordinal ->
				values.getOrElse(ordinal) {
					invalidValueError("expected an integer between 0 and ${values.size - 1}")
				}
			}
		}


		override fun JSONEncoder<JSONCodingContext>.encode(value: Value) {
			writeInt(value.ordinal)
		}
	}


	internal class StringBased<Value : Enum<*>>(
		enumClass: KClass<Value>,
		strings: List<String>
	) : EnumJSONCodec<Value>(enumClass = enumClass) {

		private val values: Array<Pair<Value, String>>


		init {
			values = enumClass.java.enumConstants
				.mapIndexed { index, value ->
					val string = strings[index]

					value to string
				}
				.toTypedArray()
		}


		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Value>) =
			readString().let { name ->
				values.firstOrNull { it.second == name }?.first
					?: invalidValueError("expected one of: ${values.joinToString { "\"${it.second}\"" }}")
			}


		override fun JSONEncoder<JSONCodingContext>.encode(value: Value) {
			writeString(values.first { it.first === value }.second)
		}
	}
}


@Suppress("FunctionName")
inline fun <reified Value : Enum<Value>> EnumJSONCodec(
	transformation: EnumJSONTransformation
) =
	EnumJSONCodec(
		enumClass = Value::class,
		transformation = transformation
	)


@Suppress("FunctionName")
fun <Value : Enum<*>> EnumJSONCodec(
	enumClass: KClass<Value>,
	transformation: EnumJSONTransformation
): EnumJSONCodec<Value> =
	when (transformation) {
		is EnumJSONTransformation.Name ->
			EnumJSONCodec.StringBased(
				enumClass = enumClass,
				strings = enumClass.java.enumConstants.map { transformation.case.convert(it.name) }
			)

		is EnumJSONTransformation.Ordinal ->
			EnumJSONCodec.OrdinalBased(enumClass = enumClass)

		is EnumJSONTransformation.ToString ->
			EnumJSONCodec.StringBased(
				enumClass = enumClass,
				strings = enumClass.java.enumConstants.map { transformation.case.convert(it.toString()) }
			)
	}
