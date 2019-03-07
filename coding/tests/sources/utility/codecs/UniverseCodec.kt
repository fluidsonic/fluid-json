package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object UniverseCodec : AbstractJSONCodec<Universe, TestCoderContext>() {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<Universe>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		readFromMapByElementValue { key ->
			when (key) {
				Keys.jaegers -> jaegers = readValueOfType()
				Keys.kaijus -> kaijus = readValueOfType()
				else -> skipValue()
			}
		}

		return Universe(
			jaegers = jaegers ?: missingPropertyError("jaegers"),
			kaijus = kaijus ?: missingPropertyError("kaijus")
		)
	}


	override fun JSONEncoder<TestCoderContext>.encode(value: Universe) {
		writeIntoMap {
			writeMapElement(Keys.jaegers, value = value.jaegers)
			writeMapElement(Keys.kaijus, value = value.kaijus)
		}
	}


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
