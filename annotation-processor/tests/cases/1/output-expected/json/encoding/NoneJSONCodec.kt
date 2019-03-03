package json.encoding

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
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

internal object NoneJSONCodec : AbstractJSONDecoderCodec<None, CustomCodingContext>() {
	override fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<in None>): None {
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
