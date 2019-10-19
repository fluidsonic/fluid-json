package json.decoding

import codecProvider.CustomCodingContext
import io.fluidsonic.json.AbstractJsonEncoderCodec
import io.fluidsonic.json.JsonEncoder
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

internal object NoneJsonCodec : AbstractJsonEncoderCodec<None, CustomCodingContext>() {
	override fun JsonEncoder<CustomCodingContext>.encode(value: None) {
		writeIntoMap {
			writeMapElement("value", string = value.value)
		}
	}
}
