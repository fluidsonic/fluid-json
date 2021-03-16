import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (Ktor Client extension)") {
	targets {
		jvm {
			dependencies {
				api(project(":fluid-json-coding"))
				api("io.ktor:ktor-client-json:1.5.2")
			}
		}
	}
}
