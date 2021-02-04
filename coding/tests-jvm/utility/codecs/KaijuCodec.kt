package tests.coding

import io.fluidsonic.json.*
import tests.coding.Kaiju.*


internal object KaijuCodec : AbstractJsonCodec<Kaiju, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JsonDecoder<TestCoderContext>.decode(valueType: JsonCodingType<Kaiju>): Kaiju {
		var breachDate: YearMonthDay? = null
		var category: Int? = null
		var height: Double? = null
		var name: String? = null
		var origin: String? = null
		var status: Status? = null
		var weight: Double? = null

		readFromMapByElementValue { key ->
			when (key) {
				Keys.breachDate -> breachDate = readValueOfType()
				Keys.category -> category = readInt()
				Keys.height -> height = readDouble()
				Keys.name -> name = readString()
				Keys.origin -> origin = readString()
				Keys.status -> status = readValueOfType()
				Keys.weight -> weight = readDouble()
				else -> skipValue()
			}
		}

		return Kaiju(
			breachDate = breachDate ?: missingPropertyError("breachDate"),
			category = category ?: missingPropertyError("category"),
			height = height ?: missingPropertyError("height"),
			name = name ?: missingPropertyError("name"),
			origin = origin ?: missingPropertyError("origin"),
			status = status ?: missingPropertyError("status"),
			weight = weight ?: missingPropertyError("weight")
		)
	}


	override fun JsonEncoder<TestCoderContext>.encode(value: Kaiju) {
		writeIntoMap {
			writeMapElement(Keys.breachDate, value = value.breachDate)
			writeMapElement(Keys.category, int = value.category)
			writeMapElement(Keys.height, double = value.height)
			writeMapElement(Keys.name, string = value.name)
			writeMapElement(Keys.origin, string = value.origin)
			writeMapElement(Keys.status, value = value.status)
			writeMapElement(Keys.weight, double = value.weight)
		}
	}


	private object Keys {

		const val breachDate = "breachDate"
		const val category = "category"
		const val height = "height"
		const val name = "name"
		const val origin = "origin"
		const val status = "status"
		const val weight = "weight"
	}


	object StatusCodec : AbstractJsonCodec<Status, TestCoderContext>() {

		override fun JsonDecoder<TestCoderContext>.decode(valueType: JsonCodingType<Status>) =
			when (val id = readString()) {
				"deceased" -> Status.deceased
				else -> invalidValueError("unknown Kaiju status: $id")
			}


		override fun JsonEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.deceased -> "deceased"
			})
		}
	}
}
