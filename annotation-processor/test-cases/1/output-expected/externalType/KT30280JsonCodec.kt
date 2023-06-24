package externalType

import codecProvider.CustomCodingContext
import codecProvider.KT30280
import io.fluidsonic.json.AbstractJsonCodec
import io.fluidsonic.json.JsonCodingType
import io.fluidsonic.json.JsonDecoder
import io.fluidsonic.json.JsonEncoder
import io.fluidsonic.json.readBooleanOrNull
import io.fluidsonic.json.readByteOrNull
import io.fluidsonic.json.readCharOrNull
import io.fluidsonic.json.readDoubleOrNull
import io.fluidsonic.json.readFloatOrNull
import io.fluidsonic.json.readIntOrNull
import io.fluidsonic.json.readLongOrNull
import io.fluidsonic.json.readShortOrNull
import io.fluidsonic.json.readStringOrNull
import io.fluidsonic.json.readValueOfType
import io.fluidsonic.json.readValueOfTypeOrNull
import io.fluidsonic.json.writeValueOrNull

internal object KT30280JsonCodec : AbstractJsonCodec<KT30280, CustomCodingContext>() {
	override fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<KT30280>): KT30280 =
			KT30280(`value` = readString())

	override fun JsonEncoder<CustomCodingContext>.encode(`value`: KT30280) {
		writeString(value.`value`)
	}
}
