package json.representation

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.readBooleanOrNull
import com.github.fluidsonic.fluid.json.readByteOrNull
import com.github.fluidsonic.fluid.json.readCharOrNull
import com.github.fluidsonic.fluid.json.readDoubleOrNull
import com.github.fluidsonic.fluid.json.readFloatOrNull
import com.github.fluidsonic.fluid.json.readIntOrNull
import com.github.fluidsonic.fluid.json.readLongOrNull
import com.github.fluidsonic.fluid.json.readShortOrNull
import com.github.fluidsonic.fluid.json.readStringOrNull
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.readValueOfTypeOrNull
import com.github.fluidsonic.fluid.json.writeValueOrNull

internal object SingleValueGenericJSONCodec : AbstractJSONCodec<SingleValueGeneric<*>,
		CustomCodingContext>() {
	override
			fun JSONDecoder<CustomCodingContext>.decode(valueType: JSONCodingType<SingleValueGeneric<*>>):
			SingleValueGeneric<*> = SingleValueGeneric(value = readValueOfTypeOrNull(valueType.arguments[0])
			as SingleValueGeneric.Bound?)
	override fun JSONEncoder<CustomCodingContext>.encode(value: SingleValueGeneric<*>) {
		writeValueOrNull(value.value)
	}
}
