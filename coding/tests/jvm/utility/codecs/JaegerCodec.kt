package tests.coding

import io.fluidsonic.json.*
import tests.coding.Jaeger.*


internal object JaegerCodec : AbstractJsonCodec<Jaeger, TestCoderContext>(
	additionalProviders = listOf(StatusCodec)
) {

	override fun JsonDecoder<TestCoderContext>.decode(valueType: JsonCodingType<Jaeger>): Jaeger {
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


	override fun JsonEncoder<TestCoderContext>.encode(value: Jaeger) {
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


	object StatusCodec : AbstractJsonCodec<Status, TestCoderContext>() {

		override fun JsonDecoder<TestCoderContext>.decode(valueType: JsonCodingType<Status>) =
			when (val id = readString()) {
				"destroyed" -> Status.destroyed
				else -> invalidValueError("unknown Jäger status: $id")
			}


		override fun JsonEncoder<TestCoderContext>.encode(value: Status) {
			writeString(when (value) {
				Status.destroyed -> "destroyed"
			})
		}
	}
}
