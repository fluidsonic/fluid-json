package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readDecodable
import com.github.fluidsonic.fluid.json.readMapByEntry
import com.github.fluidsonic.fluid.json.writeMap
import com.github.fluidsonic.fluid.json.writeMapEntry
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

		decoder.readMapByEntry { key ->
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


	override fun encode(value: Kaiju, encoder: JSONEncoder<out TestCoderContext>) {
		encoder.writeMap {
			writeMapEntry(Keys.breachDate, encodable = value.breachDate)
			writeMapEntry(Keys.category, int = value.category)
			writeMapEntry(Keys.height, double = value.height)
			writeMapEntry(Keys.name, string = value.name)
			writeMapEntry(Keys.origin, string = value.origin)
			writeMapEntry(Keys.status, encodable = value.status)
			writeMapEntry(Keys.weight, double = value.weight)
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


		override fun encode(value: Status, encoder: JSONEncoder<out TestCoderContext>) {
			encoder.writeString(when (value) {
				Status.deceased -> "deceased"
			})
		}


		override val valueClass = Status::class.java
	}
}
