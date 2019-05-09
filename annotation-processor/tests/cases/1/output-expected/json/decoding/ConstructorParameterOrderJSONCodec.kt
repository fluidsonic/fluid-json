package json.decoding

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
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
import kotlin.String
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object ConstructorParameterOrderJSONCodec :
		AbstractJSONDecoderCodec<ConstructorParameterOrder, CustomCodingContext>() {
	private val constructor: KFunction<ConstructorParameterOrder> =
			ConstructorParameterOrder::class.constructors.single { constructor ->
		if (constructor.parameters.size != 3) return@single false

		constructor.parameters.forEach { parameter ->
			val erasure = parameter.type.jvmErasure

			when (parameter.name) {
				"a" -> if (parameter.index != 1 || parameter.isVararg || erasure != String::class) return@single
						false
				"b" -> if (parameter.index != 0 || parameter.isVararg || erasure != String::class) return@single
						false
				"c" -> if (parameter.index != 2 || parameter.isVararg || erasure != String::class) return@single
						false
				else -> return@single false
			}
		}

		return@single true
	}


	private val parameter_a: KParameter = constructor.parameters.first { it.name == "a" }

	private val parameter_b: KParameter = constructor.parameters.first { it.name == "b" }

	private val parameter_c: KParameter = constructor.parameters.first { it.name == "c" }

	override
			fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<ConstructorParameterOrder>):
			ConstructorParameterOrder {
		val arguments = hashMapOf<KParameter, Any?>()
		readFromMapByElementValue { key ->
			when (key) {
				"a" -> arguments[parameter_a] = readString()
				"b" -> arguments[parameter_b] = readString()
				"c" -> arguments[parameter_c] = readString()
				else -> skipValue()
			}
		}

		return constructor.callBy(arguments)
	}
}
