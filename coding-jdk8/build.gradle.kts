import com.github.fluidsonic.fluid.library.*

fluidJvmLibraryVariant {
	description = "A JSON library written in pure Kotlin (JDK8+ coding extension)"
	jdk = JDK.v1_8
}

dependencies {
	api(project(":fluid-json-coding"))
}
