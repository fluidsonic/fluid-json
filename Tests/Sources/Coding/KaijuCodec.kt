package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readDecodable
import com.github.fluidsonic.fluid.json.readElementsFromMap
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import tests.Kaiju.Status
import java.time.LocalDate


internal object KaijuCodec : JSONCodec<Kaiju, TestCoderContext> {

	override val codecs = listOf(this, StatusCodec)
	override val valueClass = Kaiju::class.java


	override fun decode(decoder: JSONDecoder<TestCoderContext>): Kaiju {
		var breachDate: LocalDate? = null
		var category: Int? = null
		var height: Double? = null
		var name: String? = null
		var origin: String? = null
		var status: Status? = null
		var weight: Double? = null

		decoder.readElementsFromMap { key ->
			when (key) {
				Keys.breachDate -> breachDate = readDecodable()
				Keys.category -> category = readInt()
				Keys.height -> height = readDouble()
				Keys.name -> name = readString()
				Keys.origin -> origin = readString()
				Keys.status -> status = readDecodable()
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


	override fun encode(value: Kaiju, encoder: JSONEncoder<TestCoderContext>) {
		encoder.writeIntoMap {
			writeMapElement(Keys.breachDate, encodable = value.breachDate)
			writeMapElement(Keys.category, int = value.category)
			writeMapElement(Keys.height, double = value.height)
			writeMapElement(Keys.name, string = value.name)
			writeMapElement(Keys.origin, string = value.origin)
			writeMapElement(Keys.status, encodable = value.status)
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


	object StatusCodec : JSONCodec<Status, TestCoderContext> {

		override fun decode(decoder: JSONDecoder<TestCoderContext>): Status {
			val id = decoder.readString()
			return when (id) {
				"deceased" -> Status.deceased
				else -> throw JSONException("Unknown status: $id")
			}
		}


		override fun encode(value: Status, encoder: JSONEncoder<TestCoderContext>) {
			encoder.writeString(when (value) {
				Status.deceased -> "deceased"
			})
		}


		override val valueClass = Status::class.java
	}
}
