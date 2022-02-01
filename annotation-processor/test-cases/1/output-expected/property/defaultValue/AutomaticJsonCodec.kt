package `property`.defaultValue

import codecProvider.CustomCodingContext
import io.fluidsonic.json.AbstractJsonCodec
import io.fluidsonic.json.JsonCodingType
import io.fluidsonic.json.JsonDecoder
import io.fluidsonic.json.JsonEncoder
import io.fluidsonic.json.jvmErasure
import io.fluidsonic.json.missingPropertyError
import io.fluidsonic.json.readBooleanOrNull
import io.fluidsonic.json.readByteOrNull
import io.fluidsonic.json.readCharOrNull
import io.fluidsonic.json.readDoubleOrNull
import io.fluidsonic.json.readFloatOrNull
import io.fluidsonic.json.readFromMapByElementValue
import io.fluidsonic.json.readIntOrNull
import io.fluidsonic.json.readLongOrNull
import io.fluidsonic.json.readShortOrNull
import io.fluidsonic.json.readStringOrNull
import io.fluidsonic.json.readValueOfType
import io.fluidsonic.json.readValueOfTypeOrNull
import io.fluidsonic.json.writeBooleanOrNull
import io.fluidsonic.json.writeByteOrNull
import io.fluidsonic.json.writeCharOrNull
import io.fluidsonic.json.writeDoubleOrNull
import io.fluidsonic.json.writeFloatOrNull
import io.fluidsonic.json.writeIntOrNull
import io.fluidsonic.json.writeIntoMap
import io.fluidsonic.json.writeLongOrNull
import io.fluidsonic.json.writeMapElement
import io.fluidsonic.json.writeShortOrNull
import io.fluidsonic.json.writeStringOrNull
import io.fluidsonic.json.writeValueOrNull
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object AutomaticJsonCodec : AbstractJsonCodec<Automatic, CustomCodingContext>() {
	private val `constructor`: KFunction<Automatic> =
			Automatic::class.constructors.single { constructor ->
		if (constructor.parameters.size != 10) return@single false

		constructor.parameters.forEach { parameter ->
			val erasure = parameter.type.jvmErasure

			when (parameter.name) {
				"value1" -> if (parameter.index != 0 || parameter.isVararg || erasure != String::class)
						return@single false
				"value10" -> if (parameter.index != 9 || parameter.isVararg || erasure != List::class)
						return@single false
				"value2" -> if (parameter.index != 1 || parameter.isVararg || erasure != String::class)
						return@single false
				"value3" -> if (parameter.index != 2 || parameter.isVararg || erasure != String::class)
						return@single false
				"value4" -> if (parameter.index != 3 || parameter.isVararg || erasure != String::class)
						return@single false
				"value5" -> if (parameter.index != 4 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value6" -> if (parameter.index != 5 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value7" -> if (parameter.index != 6 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value8" -> if (parameter.index != 7 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value9" -> if (parameter.index != 8 || parameter.isVararg || erasure != List::class)
						return@single false
				else -> return@single false
			}
		}

		return@single true
	}


	private val parameter_value1: KParameter = constructor.parameters.first { it.name == "value1" }

	private val parameter_value10: KParameter = constructor.parameters.first { it.name == "value10" }

	private val parameter_value2: KParameter = constructor.parameters.first { it.name == "value2" }

	private val parameter_value3: KParameter = constructor.parameters.first { it.name == "value3" }

	private val parameter_value4: KParameter = constructor.parameters.first { it.name == "value4" }

	private val parameter_value5: KParameter = constructor.parameters.first { it.name == "value5" }

	private val parameter_value6: KParameter = constructor.parameters.first { it.name == "value6" }

	private val parameter_value7: KParameter = constructor.parameters.first { it.name == "value7" }

	private val parameter_value8: KParameter = constructor.parameters.first { it.name == "value8" }

	private val parameter_value9: KParameter = constructor.parameters.first { it.name == "value9" }

	public override fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<Automatic>):
			Automatic {
		val arguments = hashMapOf<KParameter, Any?>()
		readFromMapByElementValue { key ->
			when (key) {
				"value1" -> arguments[parameter_value1] = readString()
				"value10" -> arguments[parameter_value10] = readValueOfType<List<Int>>()
				"value2" -> arguments[parameter_value2] = readString()
				"value3" -> arguments[parameter_value3] = readValueOfTypeOrNull<String>()
				"value4" -> arguments[parameter_value4] = readValueOfTypeOrNull<String>()
				"value5" -> arguments[parameter_value5] = readDouble()
				"value6" -> arguments[parameter_value6] = readDouble()
				"value7" -> arguments[parameter_value7] = readValueOfTypeOrNull<Double>()
				"value8" -> arguments[parameter_value8] = readValueOfTypeOrNull<Double>()
				"value9" -> arguments[parameter_value9] = readValueOfType<List<Int>>()
				else -> skipValue()
			}
		}

		if (!arguments.containsKey(parameter_value1)) missingPropertyError("value1")
		if (!arguments.containsKey(parameter_value3)) arguments[parameter_value3] = null
		if (!arguments.containsKey(parameter_value5)) missingPropertyError("value5")
		if (!arguments.containsKey(parameter_value7)) arguments[parameter_value7] = null
		if (!arguments.containsKey(parameter_value9)) missingPropertyError("value9")

		return constructor.callBy(arguments)
	}

	public override fun JsonEncoder<CustomCodingContext>.encode(`value`: Automatic): Unit {
		writeIntoMap {
			writeMapElement("value1", string = value.value1)
			writeMapElement("value10", `value` = value.value10)
			writeMapElement("value2", string = value.value2)
			writeMapElement("value3", `value` = value.value3)
			writeMapElement("value4", `value` = value.value4)
			writeMapElement("value5", double = value.value5)
			writeMapElement("value6", double = value.value6)
			writeMapElement("value7", `value` = value.value7)
			writeMapElement("value8", `value` = value.value8)
			writeMapElement("value9", `value` = value.value9)
		}
	}
}
