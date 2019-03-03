package json.classes

import codecProvider.CustomCodingContext
import com.github.fluidsonic.fluid.json.AbstractJSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoder
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

internal object ObjectJSONCodec : AbstractJSONEncoderCodec<Object, CustomCodingContext>() {
	override fun JSONEncoder<CustomCodingContext>.encode(value: Object) {
		writeIntoMap {
			writeMapElement("value", string = value.value)
		}
	}
}
