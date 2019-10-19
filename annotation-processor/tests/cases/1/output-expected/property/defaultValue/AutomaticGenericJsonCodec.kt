package property.defaultValue

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
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object AutomaticGenericJsonCodec : AbstractJsonCodec<AutomaticGeneric<*>,
		CustomCodingContext>() {
	private val constructor: KFunction<AutomaticGeneric<*>> =
			AutomaticGeneric::class.constructors.single { constructor ->
		if (constructor.parameters.size != 4) return@single false

		constructor.parameters.forEach { parameter ->
			val erasure = parameter.type.jvmErasure

			when (parameter.name) {
				"value1" -> if (parameter.index != 0 || parameter.isVararg || erasure !=
						AutomaticGeneric.SomeInterface::class) return@single false
				"value2" -> if (parameter.index != 1 || parameter.isVararg || erasure !=
						AutomaticGeneric.SomeInterface::class) return@single false
				"value3" -> if (parameter.index != 2 || parameter.isVararg || erasure !=
						AutomaticGeneric.SomeInterface::class) return@single false
				"value4" -> if (parameter.index != 3 || parameter.isVararg || erasure !=
						AutomaticGeneric.SomeInterface::class) return@single false
				else -> return@single false
			}
		}

		return@single true
	}


	private val parameter_value1: KParameter = constructor.parameters.first { it.name == "value1" }

	private val parameter_value2: KParameter = constructor.parameters.first { it.name == "value2" }

	private val parameter_value3: KParameter = constructor.parameters.first { it.name == "value3" }

	private val parameter_value4: KParameter = constructor.parameters.first { it.name == "value4" }

	override
			fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<AutomaticGeneric<*>>):
			AutomaticGeneric<*> {
		val arguments = hashMapOf<KParameter, Any?>()
		readFromMapByElementValue { key ->
			when (key) {
				"value1" -> arguments[parameter_value1] = readValueOfType(valueType.arguments[0]) as
						AutomaticGeneric.SomeInterface
				"value2" -> arguments[parameter_value2] = readValueOfType(valueType.arguments[0]) as
						AutomaticGeneric.SomeInterface
				"value3" -> arguments[parameter_value3] = readValueOfType(valueType.arguments[0]) as
						AutomaticGeneric.SomeInterface
				"value4" -> arguments[parameter_value4] = readValueOfType(valueType.arguments[0]) as
						AutomaticGeneric.SomeInterface
				else -> skipValue()
			}
		}

		if (!arguments.containsKey(parameter_value1)) missingPropertyError("value1")
		if (!arguments.containsKey(parameter_value3)) missingPropertyError("value3")

		return constructor.callBy(arguments)
	}

	override fun JsonEncoder<CustomCodingContext>.encode(value: AutomaticGeneric<*>) {
		writeIntoMap {
			writeMapElement("value1", value = value.value1)
			writeMapElement("value2", value = value.value2)
			writeMapElement("value3", value = value.value3)
			writeMapElement("value4", value = value.value4)
		}
	}
}
