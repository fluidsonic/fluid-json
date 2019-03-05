package examples

import com.github.fluidsonic.fluid.json.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Runner > Delegate IDE build/run actions to gradle

// MyCodecProvider will be generated automatically and provides all JSONCodecs created by annotation processing in the same module.
// A custom context type can be used to provide additional information during encoding.

@JSON.CodecProvider
interface MyCodecProvider : JSONCodecProvider<MyContext>


data class MyContext(
	val authenticatedUserId: String? = null
) : JSONCodingContext
