package json.encoding

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
import json.encoding.value9
import kotlin.String

internal object AnnotatedPropertiesJSONCodec : AbstractJSONCodec<AnnotatedProperties,
		CustomCodingContext>() {
	override fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<in
			AnnotatedProperties>): AnnotatedProperties {
		var _value1: String? = null

		readFromMapByElementValue { key ->
			when (key) {
				"value1" -> _value1 = readString()
				else -> skipValue()
			}
		}

		return AnnotatedProperties(
			value1 = _value1 ?: missingPropertyError("value1")
		)
	}

	override fun JSONEncoder<CustomCodingContext>.encode(value: AnnotatedProperties) {
		writeIntoMap {
			writeMapElement("value3", string = value.value3)
			writeMapElement("value6", string = value.value6)
			writeMapElement("value9", string = value.value9)
		}
	}
}
