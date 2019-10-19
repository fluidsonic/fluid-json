import io.fluidsonic.gradle.*

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	description = "A JSON library written in pure Kotlin (JDK8+ coding extension)"
}

dependencies {
	api(project(":fluid-json-coding"))
}
