package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object UniverseCodec : AbstractJSONCodec<Universe, TestCoderContext>() {

	override fun decode(valueType: JSONCodingType<in Universe>, decoder: JSONDecoder<TestCoderContext>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Keys.jaegers -> jaegers = readValueOfType()
				Keys.kaijus -> kaijus = readValueOfType()
				else -> skipValue()
			}
		}

		return Universe(
			jaegers = jaegers ?: throw JSONException("jaegers missing"),
			kaijus = kaijus ?: throw JSONException("kaijus missing")
		)
	}


	override fun encode(value: Universe, encoder: JSONEncoder<TestCoderContext>) {
		encoder.writeIntoMap {
			writeMapElement(Keys.jaegers, value = value.jaegers)
			writeMapElement(Keys.kaijus, value = value.kaijus)
		}
	}


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
