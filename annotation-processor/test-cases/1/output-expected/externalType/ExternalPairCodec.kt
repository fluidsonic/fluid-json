package externalType

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
import kotlin.Pair
import kotlin.Unit

public object ExternalPairCodec : AbstractJsonCodec<Pair<*, *>, CustomCodingContext>() {
	public override fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<Pair<*, *>>):
			Pair<*, *> {
		var _first: Any? = null
		var _second: Any? = null

		readFromMapByElementValue { key ->
			when (key) {
				"first" -> _first = readValueOfTypeOrNull(valueType.arguments[0])
				"second" -> _second = readValueOfTypeOrNull(valueType.arguments[1])
				else -> skipValue()
			}
		}

		return Pair(
			first = _first,
			second = _second
		)
	}

	public override fun JsonEncoder<CustomCodingContext>.encode(value: Pair<*, *>): Unit {
		writeIntoMap {
			writeMapElement("first", value = value.first)
			writeMapElement("second", value = value.second)
		}
	}
}
