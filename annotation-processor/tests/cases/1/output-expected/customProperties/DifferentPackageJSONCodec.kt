package customProperties

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
import customProperties.differentPackage.writeCustomProperties3
import customProperties.writeCustomProperties2
import kotlin.String

internal object DifferentPackageJSONCodec : AbstractJSONCodec<DifferentPackage,
		CustomCodingContext>() {
	override fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<DifferentPackage>):
			DifferentPackage {
		var _value: String? = null

		readFromMapByElementValue { key ->
			when (key) {
				"value" -> _value = readString()
				else -> skipValue()
			}
		}

		return DifferentPackage(
			value = _value ?: missingPropertyError("value")
		)
	}

	override fun JSONEncoder<CustomCodingContext>.encode(value: DifferentPackage) {
		writeIntoMap {
			writeMapElement("value", string = value.value)
			value.run { this@encode.writeCustomProperties1() }
			writeCustomProperties2(value)
			writeCustomProperties3(value)
		}
	}
}
