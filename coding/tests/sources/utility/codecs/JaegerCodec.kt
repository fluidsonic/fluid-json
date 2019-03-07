package tests.coding

import com.github.fluidsonic.fluid.json.*
import tests.coding.Jaeger.Status


internal object JaegerCodec : AbstractJSONCodec<Jaeger, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<Jaeger>): Jaeger {
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
			height = height ?: missingPropertyError("height"),
			launchDate = launchDate ?: missingPropertyError("launchDate"),
			mark = mark ?: missingPropertyError("mark"),
			name = name ?: missingPropertyError("name"),
			origin = origin ?: missingPropertyError("origin"),
			status = status ?: missingPropertyError("status"),
			weight = weight ?: missingPropertyError("weight")
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

		override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<Status>): Status {
			val id = readString()
			return when (id) {
				"destroyed" -> Status.destroyed
				else -> invalidValueError("unknown Jäger status: $id")
			}
		}


		override fun JSONEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.destroyed -> "destroyed"
			})
		}
	}
}
