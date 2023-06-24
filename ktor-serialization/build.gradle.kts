import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (Ktor Client extension)") {
	targets {
		jvm {
			dependencies {
				api(project(":fluid-json-coding"))
				api("io.ktor:ktor-serialization:2.3.1")
			}
		}
	}
}
