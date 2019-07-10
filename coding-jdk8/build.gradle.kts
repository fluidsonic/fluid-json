import com.github.fluidsonic.fluid.library.*

fluidJvmLibraryVariant {
	description = "A JSON library written in pure Kotlin (JDK8+ coding extension)"
	jdk = JvmTarget.jdk8
}

dependencies {
	api(project(":fluid-json-coding"))
}
