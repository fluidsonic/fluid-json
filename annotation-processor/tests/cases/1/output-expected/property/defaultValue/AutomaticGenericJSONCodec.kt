package property.defaultValue

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.jvmErasure
import com.github.fluidsonic.fluid.json.missingPropertyError
import com.github.fluidsonic.fluid.json.readBooleanOrNull
import com.github.fluidsonic.fluid.json.readByteOrNull
import com.github.fluidsonic.fluid.json.readCharOrNull
import com.github.fluidsonic.fluid.json.readDoubleOrNull
import com.github.fluidsonic.fluid.json.readFloatOrNull
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readIntOrNull
import com.github.fluidsonic.fluid.json.readLongOrNull
import com.github.fluidsonic.fluid.json.readShortOrNull
import com.github.fluidsonic.fluid.json.readStringOrNull
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.readValueOfTypeOrNull
import com.github.fluidsonic.fluid.json.writeBooleanOrNull
import com.github.fluidsonic.fluid.json.writeByteOrNull
import com.github.fluidsonic.fluid.json.writeCharOrNull
import com.github.fluidsonic.fluid.json.writeDoubleOrNull
import com.github.fluidsonic.fluid.json.writeFloatOrNull
import com.github.fluidsonic.fluid.json.writeIntOrNull
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeLongOrNull
import com.github.fluidsonic.fluid.json.writeMapElement
import com.github.fluidsonic.fluid.json.writeShortOrNull
import com.github.fluidsonic.fluid.json.writeStringOrNull
import com.github.fluidsonic.fluid.json.writeValueOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object AutomaticGenericJSONCodec : AbstractJSONCodec<AutomaticGeneric<*>,
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
			fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<AutomaticGeneric<*>>):
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

	override fun JSONEncoder<CustomCodingContext>.encode(value: AutomaticGeneric<*>) {
		writeIntoMap {
			writeMapElement("value1", value = value.value1)
			writeMapElement("value2", value = value.value2)
			writeMapElement("value3", value = value.value3)
			writeMapElement("value4", value = value.value4)
		}
	}
}
