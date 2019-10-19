import io.fluidsonic.gradle.*

fluidJvmLibraryVariant(JvmTarget.jdk7) {
	description = "A JSON library written in pure Kotlin (Ktor Client extension)"
}

dependencies {
	api(project(":fluid-json-coding"))

	api("io.ktor:ktor-client-json-jvm:1.2.5")
}
