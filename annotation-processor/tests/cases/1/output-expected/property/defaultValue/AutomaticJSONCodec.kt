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
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object AutomaticJSONCodec : AbstractJSONCodec<Automatic, CustomCodingContext>() {
	private val constructor: KFunction<Automatic> =
			Automatic::class.constructors.single { constructor ->
		if (constructor.parameters.size != 10) return@single false

		constructor.parameters.forEach { parameter ->
			val erasure = parameter.type.jvmErasure

			when (parameter.name) {
				"value1" -> if (parameter.index != 0 || parameter.isVararg || erasure != String::class)
						return@single false
				"value10" -> if (parameter.index != 1 || parameter.isVararg || erasure != List::class)
						return@single false
				"value2" -> if (parameter.index != 2 || parameter.isVararg || erasure != String::class)
						return@single false
				"value3" -> if (parameter.index != 3 || parameter.isVararg || erasure != String::class)
						return@single false
				"value4" -> if (parameter.index != 4 || parameter.isVararg || erasure != String::class)
						return@single false
				"value5" -> if (parameter.index != 5 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value6" -> if (parameter.index != 6 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value7" -> if (parameter.index != 7 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value8" -> if (parameter.index != 8 || parameter.isVararg || erasure != Double::class)
						return@single false
				"value9" -> if (parameter.index != 9 || parameter.isVararg || erasure != List::class)
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

	override fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<Automatic>):
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

	override fun JSONEncoder<CustomCodingContext>.encode(value: Automatic) {
		writeIntoMap {
			writeMapElement("value1", string = value.value1)
			writeMapElement("value10", value = value.value10)
			writeMapElement("value2", string = value.value2)
			writeMapElement("value3", value = value.value3)
			writeMapElement("value4", value = value.value4)
			writeMapElement("value5", double = value.value5)
			writeMapElement("value6", double = value.value6)
			writeMapElement("value7", value = value.value7)
			writeMapElement("value8", value = value.value8)
			writeMapElement("value9", value = value.value9)
		}
	}
}
