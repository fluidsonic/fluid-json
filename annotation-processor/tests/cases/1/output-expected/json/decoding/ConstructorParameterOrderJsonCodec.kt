package json.decoding

import codecProvider.CustomCodingContext
import io.fluidsonic.json.AbstractJsonDecoderCodec
import io.fluidsonic.json.JsonCodingType
import io.fluidsonic.json.JsonDecoder
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
import kotlin.String
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal object ConstructorParameterOrderJsonCodec :
		AbstractJsonDecoderCodec<ConstructorParameterOrder, CustomCodingContext>() {
	private val constructor: KFunction<ConstructorParameterOrder> =
			ConstructorParameterOrder::class.constructors.single { constructor ->
		if (constructor.parameters.size != 3) return@single false

		constructor.parameters.forEach { parameter ->
			val erasure = parameter.type.jvmErasure

			when (parameter.name) {
				"a" -> if (parameter.index != 1 || parameter.isVararg || erasure != String::class)
						return@single false
				"b" -> if (parameter.index != 0 || parameter.isVararg || erasure != String::class)
						return@single false
				"c" -> if (parameter.index != 2 || parameter.isVararg || erasure != String::class)
						return@single false
				else -> return@single false
			}
		}

		return@single true
	}


	private val parameter_a: KParameter = constructor.parameters.first { it.name == "a" }

	private val parameter_b: KParameter = constructor.parameters.first { it.name == "b" }

	private val parameter_c: KParameter = constructor.parameters.first { it.name == "c" }

	override
			fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<ConstructorParameterOrder>):
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
