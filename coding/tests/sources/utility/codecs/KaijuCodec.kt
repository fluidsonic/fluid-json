package tests.coding

import com.github.fluidsonic.fluid.json.*
import tests.coding.Kaiju.Status


internal object KaijuCodec : AbstractJSONCodec<Kaiju, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<Kaiju>): Kaiju {
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


	override fun JSONEncoder<TestCoderContext>.encode(value: Kaiju) {
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


	object StatusCodec : AbstractJSONCodec<Status, TestCoderContext>() {

		override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<Status>): Status {
			val id = readString()
			return when (id) {
				"deceased" -> Status.deceased
				else -> invalidValueError("unknown Kaiju status: $id")
			}
		}


		override fun JSONEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.deceased -> "deceased"
			})
		}
	}
}
