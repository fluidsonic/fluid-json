@file:JSON.CodecProvider(name = "ExampleJSONCodecProvider")

package examples

import com.github.fluidsonic.fluid.json.*


fun main() {
	// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt.
	// Run './gradlew :fluid-json-examples:kaptKotlin' prior to running this example in order to run kapt.

	val parser = JSONCodingParser.builder()
		.decodingWith(
			ExampleJSONCodecProvider(), // generated based on annotations
			KaijuStatusJSONCodec // note that annotation-based enum coding is not yet supported
		)
		.build()

	val kaiju = parser.parseValueOfType<Kaiju>("""
		{
			"breachDate": { "year": 2015, "month": 1, "day": 12 },
			"category":   5,
			"height":     181.7,
			"name":       "Slattern",
			"origin":     "Anteverse",
			"status":     "deceased",
			"weight":     6750
		}
	""")
	println(kaiju)

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(
			ExampleJSONCodecProvider(),
			KaijuStatusJSONCodec
		)
		.build()

	println(serializer.serializeValue(kaiju))
}


@JSON.Codec
data class Kaiju(
	val breachDate: YearMonthDay,
	val category: Int,
	val height: Double,
	val name: String,
	val origin: String?,
	val status: Status,
	val weight: Double
) {

	enum class Status {

		deceased
	}
}


@JSON.Codec
data class YearMonthDay(
	val year: Int,
	val month: Int,
	val day: Int
)


internal object KaijuStatusJSONCodec : AbstractJSONCodec<Kaiju.Status, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Kaiju.Status>) =
		readString().let { id ->
			when (id) {
				"deceased" -> Kaiju.Status.deceased
				else -> invalidValueError("unknown Kaiju status: $id")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Kaiju.Status) {
		writeString(when (value) {
			Kaiju.Status.deceased -> "deceased"
		})
	}
}
