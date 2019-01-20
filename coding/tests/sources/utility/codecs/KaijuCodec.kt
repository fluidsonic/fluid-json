package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import tests.coding.Kaiju.Status


internal object KaijuCodec : AbstractJSONCodec<Kaiju, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<in Kaiju>): Kaiju {
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
			breachDate = breachDate ?: throw JSONException("breachDate missing"),
			category = category ?: throw JSONException("category missing"),
			height = height ?: throw JSONException("height missing"),
			name = name ?: throw JSONException("name missing"),
			origin = origin ?: throw JSONException("origin missing"),
			status = status ?: throw JSONException("status missing"),
			weight = weight ?: throw JSONException("weight missing")
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

		override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<in Status>): Status {
			val id = readString()
			return when (id) {
				"deceased" -> Status.deceased
				else -> throw JSONException("Unknown status: $id")
			}
		}


		override fun JSONEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.deceased -> "deceased"
			})
		}
	}
}
