package json.decoding

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
import kotlin.Unit

internal object AutomaticAnnotatedConstructorJsonCodec :
		AbstractJsonCodec<AutomaticAnnotatedConstructor, CustomCodingContext>() {
	public override
			fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<AutomaticAnnotatedConstructor>):
			AutomaticAnnotatedConstructor {
		var _value = 0L
		var value_isPresent = false

		readFromMapByElementValue { key ->
			when (key) {
				"value" ->  {
					_value = readLong()
					value_isPresent = true
				}
				else -> skipValue()
			}
		}

		value_isPresent || missingPropertyError("value")

		return AutomaticAnnotatedConstructor(
			`value` = _value
		)
	}

	public override
			fun JsonEncoder<CustomCodingContext>.encode(`value`: AutomaticAnnotatedConstructor): Unit {
		writeIntoMap {
			writeMapElement("value", string = value.`value`)
		}
	}
}
