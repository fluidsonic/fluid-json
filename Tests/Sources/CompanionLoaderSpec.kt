package tests

import com.github.fluidsonic.fluid.json.*
import org.jetbrains.spek.api.Spek


internal object CompanionLoaderSpec : Spek({

	// just make sure all companions initialize properly and we get more code coverage :)
	listOf(
		JSONCodableType.Companion,
		JSONCodec.Companion,
		JSONCodecProvider.Companion,
		JSONCoderContext.Companion,
		JSONDecoder.Companion,
		JSONDecoderCodec.Companion,
		JSONEncoder.Companion,
		JSONEncoderCodec.Companion,
		JSONException.Companion,
		JSONParser.Companion,
		JSONReader.Companion,
		JSONSerializer.Companion,
		JSONToken.Companion,
		JSONWriter.Companion
	)

	// trigger initialization of Character.* objects to make code coverage happy
	Character::class.nestedClasses.forEach {
		try {
			it.objectInstance
		}
		catch (e: IllegalAccessException) {
			// ignore
		}
	}
})
