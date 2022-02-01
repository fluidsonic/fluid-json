package json.classes

import codecProvider.CustomCodingContext
import io.fluidsonic.json.AbstractJsonCodec
import io.fluidsonic.json.JsonCodingType
import io.fluidsonic.json.JsonDecoder
import io.fluidsonic.json.JsonEncoder
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
import kotlin.Any
import kotlin.Unit

internal object GenericClassJsonCodec :
		AbstractJsonCodec<GenericClass<*, *, *>, CustomCodingContext>() {
	public override
			fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<GenericClass<*, *, *>>):
			GenericClass<*, *, *> {
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

	public override fun JsonEncoder<CustomCodingContext>.encode(`value`: GenericClass<*, *, *>): Unit {
		writeIntoMap {
			writeMapElement("a", `value` = value.a)
			writeMapElement("b", `value` = value.b)
			writeMapElement("c", `value` = value.c)
		}
	}
}
