package examples

import io.fluidsonic.json.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Runner > Delegate IDE build/run actions to gradle

// MyCodecProvider will be generated automatically and provides all JsonCodecs created by annotation processing in the same module.
// A custom context type can be used to provide additional information during encoding.

@Json.CodecProvider
interface MyCodecProvider : JsonCodecProvider<MyContext>


data class MyContext(
	val authenticatedUserId: String? = null
) : JsonCodingContext
