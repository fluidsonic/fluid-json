package json.classes

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
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
import kotlin.Any

internal object GenericClassJSONCodec : AbstractJSONCodec<GenericClass<*, *, *>,
		CustomCodingContext>() {
	override fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<GenericClass<*, *,
			*>>): GenericClass<*, *, *> {
		var _a: Any? = null
		var _b: GenericClass.Bound? = null
		var _c: Any? = null

		readFromMapByElementValue { key ->
			when (key) {
				"a" -> _a = readValueOfTypeOrNull(valueType.arguments[0])
				"b" -> _b = readValueOfType(valueType.arguments[1]) as GenericClass.Bound
				"c" -> _c = readValueOfType(valueType.arguments[2])
				else -> skipValue()
			}
		}

		return GenericClass(
			a = _a,
			b = _b ?: missingPropertyError("b"),
			c = _c ?: missingPropertyError("c")
		)
	}

	override fun JSONEncoder<CustomCodingContext>.encode(value: GenericClass<*, *, *>) {
		writeIntoMap {
			writeMapElement("a", value = value.a)
			writeMapElement("b", value = value.b)
			writeMapElement("c", value = value.c)
		}
	}
}
