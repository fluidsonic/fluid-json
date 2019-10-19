package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object EnumJsonCodecProviderTest {

	@Test
	fun test() {
		val provider = EnumJsonCodecProvider(transformation = EnumJsonTransformation.Ordinal)
		val serializer = JsonCodingSerializer.builder().encodingWith(provider).build()

		for (enumClass in listOf(Example1::class, Example2::class)) {
			assert(serializer.serializeValue(enumClass.java.enumConstants.first())).toBe("0")
		}
	}


	private enum class Example1 {
		hello1
	}


	private enum class Example2 {
		hello2
	}
}
