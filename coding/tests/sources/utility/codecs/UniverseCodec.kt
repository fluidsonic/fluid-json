package tests.coding

import io.fluidsonic.json.*


internal object UniverseCodec : AbstractJsonCodec<Universe, TestCoderContext>() {

	override fun JsonDecoder<TestCoderContext>.decode(valueType: JsonCodingType<Universe>): Universe {
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


	override fun JsonEncoder<TestCoderContext>.encode(value: Universe) {
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
