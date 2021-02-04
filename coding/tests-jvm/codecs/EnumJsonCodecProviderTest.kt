package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


@Suppress("UNCHECKED_CAST")
class EnumJsonCodecProviderTest {

	@Test
	fun test() {
		val provider = EnumJsonCodecProvider(transformation = EnumJsonTransformation.Ordinal)
		val serializer = JsonCodingSerializer.builder().encodingWith(provider).build()

		for (enumClass in listOf(Example1::class, Example2::class)) {
			expect(serializer.serializeValue(enumClass.java.enumConstants.first())).toBe("0")
		}
	}


	private enum class Example1 {
		hello1
	}


	private enum class Example2 {
		hello2
	}
}
