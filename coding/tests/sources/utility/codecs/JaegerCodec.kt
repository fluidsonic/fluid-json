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
import tests.coding.Jaeger.Status


internal object JaegerCodec : AbstractJSONCodec<Jaeger, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<in Jaeger>): Jaeger {
		var height: Double? = null
		var launchDate: YearMonthDay? = null
		var mark: Int? = null
		var name: String? = null
		var origin: String? = null
		var status: Status? = null
		var weight: Double? = null

		readFromMapByElementValue { key ->
			when (key) {
				Keys.height -> height = readDouble()
				Keys.launchDate -> launchDate = readValueOfType()
				Keys.mark -> mark = readInt()
				Keys.name -> name = readString()
				Keys.origin -> origin = readString()
				Keys.status -> status = readValueOfType()
				Keys.weight -> weight = readDouble()
				else -> skipValue()
			}
		}

		return Jaeger(
			height = height ?: throw JSONException("height missing"),
			launchDate = launchDate ?: throw JSONException("launchDate missing"),
			mark = mark ?: throw JSONException("mark missing"),
			name = name ?: throw JSONException("name missing"),
			origin = origin ?: throw JSONException("origin missing"),
			status = status ?: throw JSONException("status missing"),
			weight = weight ?: throw JSONException("weight missing")
		)
	}


	override fun JSONEncoder<TestCoderContext>.encode(value: Jaeger) {
		writeIntoMap {
			writeMapElement(Keys.height, double = value.height)
			writeMapElement(Keys.launchDate, value = value.launchDate)
			writeMapElement(Keys.mark, int = value.mark)
			writeMapElement(Keys.name, string = value.name)
			writeMapElement(Keys.origin, string = value.origin)
			writeMapElement(Keys.status, value = value.status)
			writeMapElement(Keys.weight, double = value.weight)
		}
	}


	private object Keys {

		const val height = "height"
		const val launchDate = "launchDate"
		const val mark = "mark"
		const val name = "name"
		const val origin = "origin"
		const val status = "status"
		const val weight = "weight"
	}


	object StatusCodec : AbstractJSONCodec<Status, TestCoderContext>() {

		override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<in Status>): Status {
			val id = readString()
			return when (id) {
				"destroyed" -> Status.destroyed
				else -> throw JSONException("Unknown status: $id")
			}
		}


		override fun JSONEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.destroyed -> "destroyed"
			})
		}
	}
}
