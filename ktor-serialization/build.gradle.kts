import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (Ktor serialization extension)") {
	targets {
		jvm {
			dependencies {
				api(project(":fluid-json-coding"))
				api("io.ktor:ktor-serialization:3.4.2")
			}
		}
	}
}
