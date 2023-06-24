package json.encoding

import codecProvider.CustomCodingContext
import io.fluidsonic.json.AbstractJsonDecoderCodec
import io.fluidsonic.json.JsonCodingType
import io.fluidsonic.json.JsonDecoder
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

internal object NoneJsonCodec : AbstractJsonDecoderCodec<None, CustomCodingContext>() {
	override fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<None>): None {
		var _value1: String? = null

		readFromMapByElementValue { key ->
			when (key) {
				"value1" -> _value1 = readString()
				else -> skipValue()
			}
		}

		return None(
			value1 = _value1 ?: missingPropertyError("value1")
		)
	}
}
