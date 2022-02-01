package json.representation

import codecProvider.CustomCodingContext
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
import kotlin.Unit

internal object SingleValueGenericJsonCodec :
		AbstractJsonCodec<SingleValueGeneric<*>, CustomCodingContext>() {
	public override
			fun JsonDecoder<CustomCodingContext>.decode(valueType: JsonCodingType<SingleValueGeneric<*>>):
			SingleValueGeneric<*> = SingleValueGeneric(`value` =
			readValueOfTypeOrNull(valueType.arguments[0]) as SingleValueGeneric.Bound?)

	public override fun JsonEncoder<CustomCodingContext>.encode(`value`: SingleValueGeneric<*>): Unit {
		writeValueOrNull(value.`value`)
	}
}
