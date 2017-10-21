package tests

import com.github.fluidsonic.fluid.json.Character
import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCodecProvider
import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.JSONReader
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.JSONToken
import com.github.fluidsonic.fluid.json.JSONWriter
import org.jetbrains.spek.api.Spek


internal object CompanionLoaderSpec : Spek({

	// just make sure all companions initialize properly and we get more code coverage :)
	listOf(
		JSONCodec.Companion,
		JSONCodecProvider.Companion,
		JSONCodecResolver.Companion,
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
